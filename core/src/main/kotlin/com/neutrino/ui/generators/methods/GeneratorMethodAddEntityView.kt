package com.neutrino.ui.generators.methods

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.CollapsibleWidget
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.generation.GenerationRequirements
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.ui.views.EntitiesWindow
import com.neutrino.ui.views.minor.AddGenerationRequirementsView
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.EntityName
import com.neutrino.util.name
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.*

class GeneratorMethodAddEntityView: GeneratorMethodView() {

    private var entity: Entity? = null
    private var amount: Float = 0f
    private var asPercent: Boolean = false
    private var replaceUnderneath: Boolean = false
    private val pickEntity = Container<Actor>()
    private val rulesLabel = VisLabel("")

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
        }}).growX().padLeft(16f).padRight(16f)
        mainTable.add(amountTable).padRight(8f)

        val paramsTable = VisTable()
        paramsTable.add(scene2d.visTextButton("%", "toggle").apply { onChange { asPercent = !asPercent } }).row()
        paramsTable.add(scene2d.visTextButton(".x", "toggle").apply { onChange { replaceUnderneath = !replaceUnderneath } }).row()
        mainTable.add(paramsTable)
        val rulesTable = VisTable()
        rulesTable.add(scene2d.visTextButton("rules").apply { onChange { setRules() }}).row()
        val rulesLabelTable = VisTable()
        rulesLabelTable.add(rulesLabel).left()
        rulesTable.add(rulesLabelTable)

        mainTable.add(rulesTable).growX().right().padRight(4f)
        add(mainTable).growX()
    }

    override fun addMethod(generator: GenerationAlgorithm) {
        if (entity == null)
            return
        generator.add(entity!!.name, listOf(), amount, asPercent, replaceUnderneath)
    }

    private fun setEntity(entityName: EntityName) {
        entity = Entities.new(entityName)
        pickEntity.actor = EntityButton(entity!!, false).apply {
            setSize(96f, 96f)
            onClick { stage.addActor(EntitiesWindow {setEntity(it)}) }
        }
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
                val rulesCollapsible = CollapsibleWidget(AddGenerationRequirementsView())
                rulesCollapsible.isCollapsed = false
                rulesCollapsible.width = width
                (rulesLabel.parent as VisTable).add(scene2d.visCheckBox("") {
                    isChecked = true
                    onClick { rulesCollapsible.isCollapsed = !rulesCollapsible.isCollapsed }
                })
                row()
                add(rulesCollapsible).row()
            }
        }
        scene2d.popupMenu {
            menuItem("Add new").onClick { setRuleText("new", true) }
            menuItem("Identity").subMenu().apply {
                GenerationRequirements.getIdentities().forEach {
                    menuItem(it.toString()).onClick { setRuleText(it.toString()) }
            }}
            menuItem("Entity").subMenu().apply {
                var entityHasDefaults = false
                GenerationRequirements.getEntities().forEach {
                    if (it == entity?.id)
                        entityHasDefaults = true
                }
                if (entityHasDefaults)
                    menuItem(entity?.name ?: "").onClick { setRuleText(entity!!.name) }

                GenerationRequirements.getEntities().forEach {
                    if (it != entity?.id)
                        menuItem(it.name()).onClick { setRuleText(it.name()) }
            }}
            menuItem("Others").subMenu().apply {
                GenerationRequirements.getOthers().forEach {
                    menuItem(it).onClick { setRuleText(it) }
            }}
        }.showMenu(stage, rulesLabel.parent.parent.getChild(0))
    }
}
