package com.neutrino.ui.views.minor.util

import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.Tilesets
import com.neutrino.ui.LeftTable
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.EntityId
import ktx.actors.onChange
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTextField

class TilesetTable(var tilesetName: String = ""): VisTable() {

    private val tileset: MutableMap<Identity, ArrayList<EntityId>> = mutableMapOf()
    private val tilesetTable = VisTableNested()

    init {
        add(scene2d.visTextField(tilesetName) {
            addListener(onChange { tilesetName = this.text })
        }).left().width(LeftTable.WIDTH / 2 - 32f).pad(16f)
        add(tilesetTable)
        tilesetTable.width = LeftTable.WIDTH / 2
        tilesetTable.maxHeight = 96f
        height = 96f
        if (tilesetName != "")
            tileset.putAll(Tilesets.get(tilesetName).getAll())
        fillTilesetTable()
    }

    private fun fillTilesetTable() {
        for (entities in tileset.values) {
            entities.forEach { addEntityButton(it) }
        }
    }

    private fun addEntityButton(entityId: EntityId) {
        val button = EntityButton(Entities.new(entityId), false)
        button.setSize(48f, 48f)
        tilesetTable.addNested(button)
    }
}
