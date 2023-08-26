package com.neutrino.ui.views.minor

import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.CollapsibleWidget
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.EntityPositionRequirement
import com.neutrino.generation.EntityPositionRequirementType
import com.neutrino.generation.NameOrIdentity
import com.neutrino.ui.LeftTable
import com.neutrino.ui.elements.DeleteButton
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.ui.views.EntitiesWindow
import com.neutrino.ui.views.minor.util.GenerationRulesTable
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.EntityName
import com.neutrino.util.UiManagerFactory
import com.neutrino.util.add
import com.neutrino.util.remove
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AddGenerationRequirementsView: VisTable() {

    private val ruleGroups = ArrayList<RuleGroup>()
    // Save params
    private var saveName: String? = null
    private var saveEntity: EntityName? = null
    private var saveIdentity: Identity? = null
    private val pickEntity = Container<Actor>()

    init {
        addSaveAsTable()
        addRuleGroup().addRulePicker(false)
    }

    private fun addSaveAsTable() {
        val saveAsTable = VisTable()
        val saveParamsTable = VisTable()
        val saveParamsCollapsible = CollapsibleWidget(saveParamsTable, true)
        saveAsTable.add(scene2d.visLabel("Save rules as defaults?")).padLeft(16f).grow().center()
        saveAsTable.add(scene2d.visCheckBox("") {
            isChecked = false
            onClick { saveParamsCollapsible.isCollapsed = !saveParamsCollapsible.isCollapsed }
        }).padRight(16f)

        val firstCol = VisTable()
        firstCol.add(scene2d.visLabel("as others")).expandX().center().top().row()
        firstCol.add(scene2d.visTextField {
            onChange {
                saveName = text
                if (text.isEmpty())
                    saveName = null
            }
        }).growX().bottom()
        val secondCol = VisTable()
        secondCol.add(scene2d.visLabel("entity")).expandX().center().top().row()
        pickEntity.size(64f)
        pickEntity.actor = getPickEntityButton()
        secondCol.add(pickEntity).expandX().center().bottom()
        val thirdCol = VisTable()
        val identityLabel = VisLabel("")
        thirdCol.add(scene2d.visLabel("identity") {
            onClick {
                scene2d.popupMenu {
                    menuItem("None").onClick {
                        identityLabel.setText("")
                        saveIdentity = null
                    }
                    Identity::class.nestedClasses.map { it.simpleName }.minus("Any").forEach { className ->
                        menuItem(className!!) { onClick {
                            identityLabel.setText(className!!)
                            saveIdentity = Identity::class.nestedClasses
                                .find { it.simpleName == className }?.createInstance() as Identity?
                        } }
                    }
                }.showMenu(stage, this)
            }
        }).expand().center().top().row()
        thirdCol.add(identityLabel).expandX().center().bottom()

        saveParamsTable.add(firstCol).growX().left().uniform()
        saveParamsTable.add(secondCol).growX().uniform()
        saveParamsTable.add(thirdCol).growX().right()
        add(saveAsTable).growX().row()
        add(saveParamsCollapsible).growX().row()
    }

    private fun addRuleGroup(): RuleGroup {
        val ruleGroup = RuleGroup()
        ruleGroups.add(ruleGroup)
        add(ruleGroup).pad(16f).row()
        return ruleGroup
    }

    fun getRules(): List<EntityPositionRequirement> {
        val rules: ArrayList<EntityPositionRequirement> = ArrayList()
        for (group in ruleGroups) {
            if (group.requirementType != null && group.rules.size > 1)
                rules.add(EntityPositionRequirement(group.requirementType!!))
            for (i in 0 until group.rules.size - 1) {
                rules.add(group.rules[i].getRules())
            }
        }

        println("SAVE: \n\tName: $saveName\n\tEntity: $saveEntity\n\tIdentity: $saveIdentity")
        return rules
    }

    private fun setEntity(entityName: EntityName) {
        saveEntity = entityName
        pickEntity.actor = EntityButton(Entities.new(entityName), false).apply {
            setSize(64f, 64f)
            onClick { stage.addActor(EntitiesWindow {setEntity(it)}) }
            addListener(object : ClickListener(Buttons.RIGHT) {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    deleteEntity()
                }
            })
        }
    }

    private fun getPickEntityButton(): VisTextButton {
        return scene2d.visTextButton("+") {
            width = 64f
            height = 64f
            onClick { stage.addActor(EntitiesWindow { setEntity(it)}) }
            addListener(object : ClickListener(Buttons.RIGHT) {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    deleteEntity()
                }
            })
        }
    }

    private fun deleteEntity() {
        scene2d.popupMenu {
            menuItem("delete").onClick {
                saveEntity = null
                pickEntity.actor = getPickEntityButton()
            }
        }.showMenu(stage, pickEntity)
    }

    override fun getPrefWidth(): Float {
        return LeftTable.WIDTH - 64f
    }

    private open inner class RuleGroup: VisTable() {

        private val ruleGroupButton = VisTextButton("Add rule group")
        var requirementType: EntityPositionRequirementType? = null
        var rules = ArrayList<PositionRequirementRule>()
        private val rulesTable = VisTableNested()
        private val deleteButton: DeleteButton

        init {
            add(ruleGroupButton.apply { onClick {
                scene2d.popupMenu {
                    menuItem("AND").onClick { initializeNewOnFirst(EntityPositionRequirementType.AND) }
                    menuItem("NAND").onClick { initializeNewOnFirst(EntityPositionRequirementType.NAND) }
                    menuItem("OR").onClick { initializeNewOnFirst(EntityPositionRequirementType.OR) }
                    menuItem("NOR").onClick { initializeNewOnFirst(EntityPositionRequirementType.NOR) }
                }.showMenu(stage, ruleGroupButton)
            } }).expandX().center()
            deleteButton = DeleteButton {
                ruleGroups.remove(this)
                this@AddGenerationRequirementsView.remove(this)

                UiManagerFactory.getUI().generateMap()
            }
            add(deleteButton).top().right()
            deleteButton.isVisible = false
            row()
            add(rulesTable).expandX().left().row()
            rulesTable.width = LeftTable.WIDTH - 64f
            rulesTable.padLRTB(0f, 16f, 8f, 0f)
        }

        fun initializeNewOnFirst(type: EntityPositionRequirementType) {
            ruleGroupButton.setText(type.name)
            requirementType = type
            UiManagerFactory.getUI().generateMap()
            if (rules.size != 0)
                return
            addRulePicker(false)
            addRuleGroup()
        }

        fun addRulePicker(addNewGroup: Boolean) {
            val newRule = PositionRequirementRule()
            rules.add(newRule)
            rulesTable.addNested(newRule)
            if (addNewGroup)
                addRuleGroup()
            if (ruleGroups.size != 1 && rules.size != 0)
                deleteButton.isVisible = true
        }

        internal inner class PositionRequirementRule: RulePickerButton() {

            private var ruleType: EntityPositionRequirementType = EntityPositionRequirementType.AND
            private var positionList: List<Int> = listOf()
            private var entity: EntityName? = null
            private var identity: KClass<out Identity>? = null

            init {
                setSize(72f, 72f)
                onClick {
                    stage.addActor(GenerationRulesTable(
                        NameOrIdentity(entity, identity, false), ruleType, positionList.toSet()) {
                        ruleType = it.first
                        entity = it.second?.getEntityName()
                        identity = it.second?.identity
                        positionList = it.third.first

                        val notType = ruleType == EntityPositionRequirementType.NAND ||
                            ruleType == EntityPositionRequirementType.NOR

                        for (i in it.third.second.textureList.indices) {
                            setTexture(i, it.third.second.textureList[i])
                            setNot(i, notType && positionList.find { it == i } != null)
                        }

                        UiManagerFactory.getUI().generateMap()
                    })
                    if (rules.indexOf(this@PositionRequirementRule) == rules.size - 1)
                        addRulePicker(ruleGroups.size == 1 && rules.size == 1)
                }
            }

            fun getRules(): EntityPositionRequirement? {
                if (positionList.isEmpty())
                    return null
                if (entity != null)
                    return EntityPositionRequirement(ruleType, entity!!, positionList.map { it + 1 })
                if (identity != null)
                    return EntityPositionRequirement(ruleType, identity!!.createInstance(), positionList.map { it + 1 })
                return null
            }
        }
    }
}
