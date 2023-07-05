package com.neutrino.ui.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter
import com.kotcrab.vis.ui.widget.*
import com.neutrino.entities.attributes.Identity
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.attributes.TextureAttributeView
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AddNewEntityView: VisTable() {

    private val saveButton = VisTextButton("save")
    private val nameTextField = VisTextField()
    private val identityTable = VisTableNested()
    private val identityList = Identity::class.nestedClasses.map { it.simpleName }
    private val attributeTable = VisTable()
    private val addedAttributes = mutableSetOf<AttributeView>()
    private val attributeList = mapOf<String, KClass<out AttributeView>>(
        "TextureAttribute" to TextureAttributeView::class,
    )

    init {
        TableUtils.setSpacingDefaults(this)
        columnDefaults(0).left()
        padTop(0f)
        top()

        val title = ViewTitle("Add new entity") {
            println("BackButtonClicked")
        }
        saveButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                saveEntity()
            }
        })
        saveButton.isDisabled = true
        nameTextField.setTextFieldListener { _, _ -> validateSave() }
        title.add(saveButton).right()

        add(title).growX().padTop(16f).row()
        add(nameTextField).growX().padLeft(32f).padRight(32f).padTop(32f).row()
        identityTable.columnDefaults(0).left()
        identityTable.left()
        identityTable.add(getAddButton())
        add(identityTable).growX().padTop(16f).padLeft(32f).padRight(32f).row()
        add(attributeTable).growX().padTop(16f).row()

        val attributeParamsTable = VisTable()
        val addAttributeButton = VisTextButton("+")
        attributeParamsTable.add(addAttributeButton).left().padLeft(32f)
        attributeParamsTable.add(VisLabel("Add Attribute")).left().padLeft(32f)
        add(attributeParamsTable).row()
        addAttributeButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val menu = PopupMenu()
                val remainingAttributes = attributeList.keys.minus(addedAttributes.map { it.attributeName }.toSet())
                remainingAttributes.forEach {
                    menu.addItem(MenuItem(it.toString(), object : ChangeListener() {
                        override fun changed(event: ChangeEvent?, actor: Actor?) {
                            addAttribute(attributeList[it.toString()]!!)
                            validateSave()
                        }
                    }))
                }
                menu.showMenu(stage, actor)
            }
        })
    }

    fun addAttribute(attribute: KClass<out AttributeView>) {
        val attributeParamsTable = VisTable()
        val attributeView = attribute.createInstance()
        val attributeCheckBox = VisCheckBox("")
        attributeParamsTable.background = nameTextField.style.background
        attributeCheckBox.isChecked = true
        attributeCheckBox.addListener(attributeView.getCollapseListener())
        val deleteButton = VisImageButton(VisUI.getSkin().getDrawable("icon-close"))
        deleteButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Dialogs.showOptionDialog(stage,
                    "Delete ${attributeView.attributeName}?",
                    "",
                    Dialogs.OptionDialogType.YES_NO,
                    object : OptionDialogAdapter() {
                        override fun yes() {
                            attributeTable.removeActor(attributeParamsTable)
                            attributeTable.removeActor(attributeView)
                            addedAttributes.remove(attributeView)
                            validateSave()
                        }
                    })
            }
        })

        attributeParamsTable.add(VisLabel(attributeView.attributeName)).expandX().left().padLeft(32f)
        attributeParamsTable.add(deleteButton).right().padRight(16f)
        attributeParamsTable.add(attributeCheckBox).right()
        attributeTable.add(attributeParamsTable).growX().row()
        attributeTable.add(attributeView).growX().row()
        addedAttributes.add(attributeView)
    }

    override fun getPrefHeight(): Float {
//        return children.sumOf { (it as Layout).prefHeight.toDouble() }.toFloat() + 128f
        return children.sumOf { it.height.toDouble() }.toFloat() + 128f
    }

    private fun getIdentityList(): PopupMenu {
        val menu = PopupMenu()

        val remainingIdentities = identityList.minus(identityTable.getElements().map { (it as? VisTextButton)?.text.toString() }.toSet())

        remainingIdentities.forEach {
            if (it != null)
                menu.addItem(identityMenuItem(it.toString()))
        }

        return menu
    }

    private fun identityMenuItem(name: String): MenuItem {
        return MenuItem(name, object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val newIdentity = VisTextButton(name, object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        identityTable.removeLast()
                        identityTable.removeNested(actor!!)
                        identityTable.addNested(getAddButton())
                    }
                })
                identityTable.removeLast()
                identityTable.addNested(newIdentity)
                identityTable.addNested(getAddButton())
            }
        })
    }

    private fun getAddButton(): VisTextButton {
        return VisTextButton("+", object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                getIdentityList().showMenu(stage, actor)
            }
        })
    }

    private fun validateSave() {
        var enabled = true
        if (nameTextField.text.isEmpty())
            enabled = false
        if (addedAttributes.size == 0)
            enabled = false
        for (attribute in addedAttributes) {
            if (!attribute.validateAttribute()) {
                enabled = false
                return
            }
        }
        saveButton.isDisabled = !enabled
    }

    private fun saveEntity() {
        val entitiesFile = Gdx.files.local("assets/core/AddEntities.kts")
        val builder = StringBuilder(300)
        builder.append("Entities.add(\"${nameTextField.text}\") {\n\tEntity()\n")

        val identities = identityTable.getElements().map { (it as? VisTextButton)?.text.toString() }
        if (identities.size > 1)
            builder.append("\t\t")
        for (i in 0 until identities.size - 1) {
            builder.append(".addAttribute(Identity.${identities[i]}())")
        }
        if (identities.size > 1)
            builder.append("\n")

        for (attribute in addedAttributes) {
            attribute.onSaveAction()
            builder.append("\t\t.addAttribute(")
            builder.append(attribute.generateString())
            builder.append(")\n")
        }
        builder.append("}")

        entitiesFile.writeString(builder.toString(), true)
        println(builder.toString())
    }
}
