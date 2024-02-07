package com.neutrino.ui.attributes

import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable

class MapParamsAttributeView: AttributeView(VisTable()) {

    override val attributeName: String = "MapParams"
    private val allowOnTopCheckbox = VisCheckBox("")
    private val allowCharacterOnTopCheckbox = VisCheckBox("")

    init {
        TableUtils.setSpacingDefaults(table)
        table.padTop(16f)
        table.padBottom(16f)
        val firstRow = VisTable()
        firstRow.add(allowOnTopCheckbox).left().padLeft(32f)
        firstRow.add(VisLabel("Allow on top")).left().padLeft(32f).growX()
        val secondRow = VisTable()
        secondRow.add(allowCharacterOnTopCheckbox).left().padLeft(32f)
        secondRow.add(VisLabel("Allow character on top")).left().padLeft(32f).growX()
        table.add(firstRow).growX().row()
        table.add(secondRow).growX()
    }

    override fun generateString(): String {
        return "MapParams(${allowOnTopCheckbox.isChecked}, ${allowCharacterOnTopCheckbox.isChecked})"
    }
}
