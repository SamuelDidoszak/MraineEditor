package com.neutrino.ui.views

import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.generation.NameOrIdentity
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.views.util.Callback
import com.neutrino.ui.views.minor.RulesTable
import com.neutrino.util.getChangeListener
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTable

class AddRulesView(
    ruleList: MutableList<NameOrIdentity?>? = null,
    override val callback: (data: Triple<List<NameOrIdentity?>, RulePickerButton, Triple<Boolean, Boolean, Boolean>>) -> Unit
): VisWindow(""), Callback<Triple<List<NameOrIdentity?>, RulePickerButton, Triple<Boolean, Boolean, Boolean>>> {

    private val rulesTable = RulesTable(ruleList)
    private var flipX = false
    private var flipY = false
    private var checkAll = false

    init {
        isModal = true
        closeOnEscape()
        addCloseButton()

        val flipTable = VisTable()
        val checkAllButton = VisTextButton("Check all", "toggle")
        checkAllButton.addListener(getChangeListener { _, _ ->
            checkAll = !checkAll
        })
        checkAllButton.isVisible = false
        val flipXButton = VisTextButton("Flip X", "toggle")
        flipXButton.addListener(getChangeListener { _, _ ->
            flipX = !flipX
            if (!flipX && !flipY) {
                checkAllButton.isVisible = false
                if (checkAll)
                    checkAllButton.toggle()
                checkAll = false
            } else
                checkAllButton.isVisible = true
        })
        val flipYButton = VisTextButton("Flip Y", "toggle")
        flipYButton.addListener(getChangeListener { _, _ ->
            flipY = !flipY
            if (!flipX && !flipY) {
                checkAllButton.isVisible = false
                if (checkAll)
                    checkAllButton.toggle()
                checkAll = false
            } else
                checkAllButton.isVisible = true
        })

        flipTable.add(flipXButton).center().padBottom(32f).row()
        flipTable.add(flipYButton).center().padBottom(32f).row()
        flipTable.add(checkAllButton).center()

        val saveButton = VisTextButton("save")
        saveButton.addListener(getChangeListener { _, _ -> save() })
        val closeButton = VisTextButton("close")
        closeButton.addListener(getChangeListener { _, _ -> close() })

        add(scene2d.visTable {
            add(rulesTable).padRight(32f)
            add(flipTable).center().padRight(32f)
        }).row()
        add(scene2d.visTable {
            add(closeButton).padRight(48f)
            add(saveButton)
        }).center().padTop(32f).padBottom(16f)

        pack()
        centerWindow()
    }

    private fun save() {
        val rules = rulesTable.getRules()
        callback.invoke(Triple(rules.first, rules.second, Triple(flipX, flipY, checkAll)))
        close()
    }
}
