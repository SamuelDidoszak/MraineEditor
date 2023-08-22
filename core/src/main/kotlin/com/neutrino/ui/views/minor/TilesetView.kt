package com.neutrino.ui.views.minor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.generation.Tilesets
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.views.minor.util.TilesetTable
import ktx.actors.onClick

class TilesetView: AttributeView(VisTable()) {

    override val attributeName: String = "Tileset"
    val tilesets = mutableSetOf<String>()
    private val tilesetTables = ArrayList<TilesetTable>()
    private val addButton = VisTextButton("+")

    init {
        table.add(addButton).padTop(16f).padLeft(16f).left().row()
        addButton.onClick { addTilesetMenu(addButton) }
    }

    private fun addTilesetTable(name: String) {
        val tilesetTable = TilesetTable(name)
        tilesetTables.add(tilesetTable)
        tilesets.add(name)
        table.removeActor(addButton)
        table.add(tilesetTable).growX().padTop(16f).row()
        table.add(addButton).left().padTop(16f).padLeft(16f).row()
    }

    private fun addTilesetMenu(actor: Actor) {
        val menu = PopupMenu()
        val remainingIdentities = Tilesets.getAllTilesetNames().minus(tilesets)

        remainingIdentities.forEach {
            menu.addItem(getTilesetMenuItem(it))
        }
        menu.showMenu(stage, actor)
    }

    private fun getTilesetMenuItem(name: String): MenuItem {
        return MenuItem(name, object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                addTilesetTable(name)
            }
        })
    }

    override fun generateString(): String {
        TODO("Not yet implemented")
    }
}














