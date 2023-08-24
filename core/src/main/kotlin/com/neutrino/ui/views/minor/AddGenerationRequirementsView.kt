package com.neutrino.ui.views.minor

import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.generation.EntityPositionRequirementType
import com.neutrino.ui.LeftTable
import com.neutrino.ui.elements.DeleteButton
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.util.remove
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.popupMenu

class AddGenerationRequirementsView: VisTable() {

    private val ruleGroups = ArrayList<RuleGroup>()

    init {
        addRuleGroup().addRulePicker(false)
    }

    private fun addRuleGroup(): RuleGroup {
        val ruleGroup = RuleGroup()
        ruleGroups.add(ruleGroup)
        add(ruleGroup).pad(16f).row()
        return ruleGroup
    }

    override fun getPrefWidth(): Float {
        return LeftTable.WIDTH - 64f
    }

    private inner class RuleGroup: VisTable() {

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
            }
            add(deleteButton).top().right()
            deleteButton.isVisible = false
            row()
            add(rulesTable).left().row()
            rulesTable.width = LeftTable.WIDTH - 64f
            rulesTable.padLRTB(0f, 16f, 8f, 0f)
        }

        fun initializeNewOnFirst(type: EntityPositionRequirementType) {
            ruleGroupButton.setText(type.name)
            requirementType = type
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

        private inner class PositionRequirementRule: RulePickerButton() {

            var ruleType: EntityPositionRequirementType = EntityPositionRequirementType.AND

            init {
                setSize(72f, 72f)
                onClick {
                    stage.addActor(RulesTable())
                    if (rules.indexOf(this@PositionRequirementRule) == rules.size - 1)
                        addRulePicker(ruleGroups.size == 1 && rules.size == 1)
                }
            }
        }
    }
}
