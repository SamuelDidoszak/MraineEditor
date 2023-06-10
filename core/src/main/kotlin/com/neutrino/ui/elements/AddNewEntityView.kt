package com.neutrino.ui.elements

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.*
import com.neutrino.entities.attributes.Identity
import com.neutrino.ui.attributes.TextureAttributeView

class AddNewEntityView: VisWindow("Add New Entity") {

    private val nameTextField = VisTextField()
    private val identityTable = VisTableNested()
    private val identityList = Identity::class.nestedClasses.map { it.simpleName }

    init {
        setSize(width, height)
        TableUtils.setSpacingDefaults(this)
        columnDefaults(0).left()
        add(nameTextField).row()
        identityTable.columnDefaults(0).left()
        identityTable.left()
        identityTable.add(getAddButton())
        add(identityTable).growX().row()

        val attributeCheckBox = VisCheckBox("TextureAttribute")
        val attributeView = TextureAttributeView()
        attributeCheckBox.addListener(attributeView.getCollapseListener())
        add(attributeCheckBox).row()
        add(attributeView).growX().row()
    }

    private fun getIdentityList(): PopupMenu {
        val menu = PopupMenu()

        val remainingIdentities = identityList.minus(identityTable.getElements().map { (it as? VisTextButton)?.text.toString() }.toSet())

        remainingIdentities.forEach {
            if (it != null)
                menu.addItem(identityMenuItem(it.toString()))
        }

        return menu

//        menu.addItem(identityMenuItem("jeff"))
//        menu.addItem(identityMenuItem("jeff2"))
//        menu.addItem(identityMenuItem("ahtnahtniasrhtieranstarbs"))
//        val subMenu = MenuItem("subMenu")
//        subMenu.subMenu {
//            MenuItem("sub1")
//            MenuItem("uke")
//        }
//        menu.addItem(subMenu)
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
}
