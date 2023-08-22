package com.neutrino.ui.views

import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextField
import com.neutrino.generation.MapTag
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.elements.TitleView
import com.neutrino.ui.views.minor.TilesetView
import com.neutrino.ui.views.util.Callback
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class TagsView(
    private val editTag: MapTag? = null,
    override val callback: (data: MapTag) -> Unit = {}
): VisTable(), Callback<MapTag> {

    private val title: TitleView
    private val nameTextField = VisTextField()
    private val tagElementsTable = VisTable()
    private val tagElements = ArrayList<AttributeView>()

    init {
        TableUtils.setSpacingDefaults(this)
        columnDefaults(0).left()
        padTop(0f)
        top()

        title = TitleView("Add new tag", false, "save") {

        }
        title.rightButton.isDisabled = true

        if (editTag != null)
            nameTextField.text = "Editing"
        nameTextField.setTextFieldListener { _, _ -> validateSave() }

        add(title).growX().padTop(16f).row()
        add(nameTextField).growX().padLeft(32f).padRight(32f).padTop(32f).row()
        add(tagElementsTable).growX().padTop(16f).row()

        addTagElement(TilesetView::class)
    }


    private fun addTagElement(attribute: KClass<out AttributeView>) {
        val attributeParamsTable = VisTable()
        val attributeView = attribute.createInstance()
        val attributeCheckBox = VisCheckBox("")
        attributeParamsTable.background = nameTextField.style.background
        attributeCheckBox.isChecked = true
        attributeCheckBox.addListener(attributeView.getCollapseListener())

        attributeParamsTable.add(VisLabel(attributeView.attributeName)).expandX().left().padLeft(32f)
        attributeParamsTable.add(attributeCheckBox).right()
        tagElementsTable.add(attributeParamsTable).growX().row()
        tagElementsTable.add(attributeView).growX().row()
        tagElements.add(attributeView)
    }

    private fun validateSave() {

    }
}


























