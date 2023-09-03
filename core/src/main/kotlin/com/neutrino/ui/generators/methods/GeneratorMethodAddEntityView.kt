package com.neutrino.ui.generators.methods

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.CollapsibleWidget
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.builders.GenerationRequirementBuilder
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.generation.EntityPositionRequirement
import com.neutrino.generation.GenerationRequirements
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.ui.views.EntitiesWindow
import com.neutrino.ui.views.minor.AddGenerationRequirementsView
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.EntityName
import com.neutrino.util.UiManagerFactory
import com.neutrino.util.name
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.*

open class GeneratorMethodAddEntityView: GeneratorMethodView() {

    internal var entity: Entity? = null
    internal var amount: Float = 0f
    internal var asPercent: Boolean = false
    internal var replaceUnderneath: Boolean = false
    private val pickEntity = Container<Actor>()
    private val rulesLabel = VisLabel("")
    private var ruleType: RuleType = RuleType.None

    init {
        val mainTable = VisTable()
        pickEntity.size(96f)
        pickEntity.actor = scene2d.visTextButton("Entity") {
            width = 96f
            height = 96f
            onClick { stage.addActor(EntitiesWindow { setEntity(it)}) }
        }
        mainTable.add(pickEntity).width(64f).height(64f).padLeft(16f).padRight(16f)
        val amountTable = VisTable()
        amountTable.add(scene2d.visLabel("Amount")).row()
        amountTable.add(scene2d.visTextField {onChange {
            try {
                amount = text.toString().toFloat()
            } catch (e: NumberFormatException) {
                amount = 0f
            }
            UiManagerFactory.getUI().generateMap()
        }}).growX().padLeft(16f).padRight(16f)
        mainTable.add(amountTable).padRight(8f)

        val paramsTable = VisTable()
        paramsTable.add(scene2d.visTextButton("%", "toggle").apply { onChange {
            asPercent = !asPercent
            UiManagerFactory.getUI().generateMap()
        } }).row()
        paramsTable.add(scene2d.visTextButton(".x", "toggle").apply { onChange {
            replaceUnderneath = !replaceUnderneath
            UiManagerFactory.getUI().generateMap()
        } }).row()

        mainTable.add(paramsTable)
        val rulesTable = VisTable()
        rulesTable.add(scene2d.visTextButton("rules").apply { onChange { setRules() }}).row()
        val rulesLabelTable = VisTable()
        rulesLabelTable.add(rulesLabel).left()
        rulesTable.add(rulesLabelTable)

        mainTable.add(rulesTable).growX().right().padRight(4f)
        add(mainTable).growX()
    }

    open override fun addMethod(generator: GenerationAlgorithm) {
        if (entity == null)
            return
        generator.add(entity!!.name, getRules(), amount, asPercent, replaceUnderneath)
    }

    open override fun generateString(): String {
        val builder = StringBuilder(100)
        builder.append(".add(\"${entity!!.name}\", ")
        addRulesString(builder)
        addParametersString(builder)
        return builder.toString()
    }

    internal fun addRulesString(builder: StringBuilder) {
        when (ruleType) {
            is RuleType.Entity -> {
                val entityName = (ruleType as RuleType.Entity).entityName
                if (entityName != entity!!.name)
                    builder.append("\"$entityName\", ")
            }
            is RuleType.Identity ->
                builder.append("Identity.${(ruleType as RuleType.Identity).identity::class.simpleName}(), ")
            is RuleType.Other ->
                builder.append("true, \"${(ruleType as RuleType.Other).ruleName}\", ")
            is RuleType.New -> {
                val addRules = (ruleType as RuleType.New).addRules
                if (addRules.saveIdentity != null)
                    builder.append("Identity.${addRules.saveIdentity!!::class.simpleName}(), ")
                else if (addRules.saveEntity != null) {
                    if (addRules.saveEntity != entity!!.name)
                        builder.append("\"${addRules.saveEntity!!}\", ")
                }
                else if (addRules.saveName != null)
                    builder.append("true, ${addRules.saveName!!}, ")
                else {
                    val rules = addRules.getRules()
                    builder.append("listOf(")
                    for (rule in rules) {
                        builder.append("\n\t")
                        builder.append("$rule,")
                    }
                    if (rules.isNotEmpty())
                        builder.append("\n")
                    builder.append("), ")
                }
            }
            is RuleType.None -> builder.append("listOf(), ")
        }
    }

    internal fun addParametersString(builder: StringBuilder) {
        if (!asPercent)
            builder.append("${amount.toInt()}f, ")
        else
            builder.append("${amount}f, ")
        builder.append("$asPercent, $replaceUnderneath)")
    }

    fun save() {
        if (ruleType !is RuleType.New)
            return
        val addRules = (ruleType as RuleType.New).addRules
        if (addRules.saveName == null && addRules.saveEntity == null && addRules.saveIdentity == null)
            return
        GenerationRequirementBuilder().buildRules(addRules).save()
    }

    internal fun getRules(): List<EntityPositionRequirement> {
        return when (ruleType) {
            is RuleType.Entity -> GenerationRequirements.get((ruleType as RuleType.Entity).entityName)
            is RuleType.Identity -> GenerationRequirements.get((ruleType as RuleType.Identity).identity)
            is RuleType.Other -> GenerationRequirements.getOther((ruleType as RuleType.Other).ruleName)
            is RuleType.New -> (ruleType as RuleType.New).addRules.getRules()
            is RuleType.None -> listOf()
        }
    }

    private fun setEntity(entityName: EntityName) {
        entity = Entities.new(entityName)
        pickEntity.actor = EntityButton(entity!!, false).apply {
            setSize(96f, 96f)
            onClick { stage.addActor(EntitiesWindow {setEntity(it)}) }
        }

        UiManagerFactory.getUI().generateMap()
    }

    private fun setRules() {
        fun setRuleText(text: String, addCollapse: Boolean = false) {
            rulesLabel.setText(text)
            if (text.length > 8)
                rulesLabel.setText(text.substring(0, 8))

            if (rulesLabel.parent.getChild(rulesLabel.parent.children.size - 1) is VisCheckBox) {
                rulesLabel.parent.removeActorAt(rulesLabel.parent.children.size - 1, true)
                removeActorAt(children.size - 1, true)
            }

            if (addCollapse) {
                val rulesView = AddGenerationRequirementsView()
                val rulesCollapsible = CollapsibleWidget(rulesView)
                rulesCollapsible.isCollapsed = false
                rulesCollapsible.width = width
                (rulesLabel.parent as VisTable).add(scene2d.visCheckBox("") {
                    isChecked = true
                    onClick { rulesCollapsible.isCollapsed = !rulesCollapsible.isCollapsed }
                })
                row()
                add(rulesCollapsible).row()
                ruleType = RuleType.New(rulesView)
            }

            UiManagerFactory.getUI().generateMap()
        }
        scene2d.popupMenu {
            menuItem("Add new").onClick { setRuleText("new", true) }
            menuItem("Identity").subMenu().apply {
                GenerationRequirements.getIdentities().forEach {
                    menuItem(it.toString()).onClick {
                        ruleType = RuleType.Identity(it)
                        setRuleText(it.toString())
                    }
            }}
            menuItem("Entity").subMenu().apply {
                var entityHasDefaults = false
                GenerationRequirements.getEntities().forEach {
                    if (it == entity?.id)
                        entityHasDefaults = true
                }
                if (entityHasDefaults)
                    menuItem(entity?.name ?: "").onClick {
                        ruleType = RuleType.Entity(entity!!.name)
                        setRuleText(entity!!.name)
                    }

                GenerationRequirements.getEntities().forEach {
                    if (it != entity?.id)
                        menuItem(it.name()).onClick {
                            ruleType = RuleType.Entity(it.name())
                            setRuleText(it.name())
                        }
            }}
            menuItem("Others").subMenu().apply {
                GenerationRequirements.getOthers().forEach {
                    menuItem(it).onClick {
                        ruleType = RuleType.Other(it)
                        setRuleText(it)
                    }
            }}
        }.showMenu(stage, rulesLabel.parent.parent.getChild(0))
    }

    private sealed class RuleType() {
        data class Entity(val entityName: EntityName): RuleType()
        data class Identity(val identity: com.neutrino.entities.attributes.Identity): RuleType()
        data class Other(val ruleName: String): RuleType()
        data class New(val addRules: AddGenerationRequirementsView): RuleType()
        object None: RuleType()
    }
}
