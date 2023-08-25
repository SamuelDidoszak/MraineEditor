package com.neutrino.ui.generators

import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.generation.algorithms.SquidGenerationAlgorithm
import com.neutrino.generation.util.GenerationParams
import com.neutrino.util.UiManagerFactory
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.subMenu
import ktx.scene2d.vis.visLabel
import squidpony.squidgrid.mapping.styled.TilesetType

class SquidGenerationAlgorithmView(): GenerationAlgorithmView() {

    var tilesetType: TilesetType = TilesetType.DEFAULT_DUNGEON
    companion object {
        private val tilesetTypes: Map<String, List<TilesetType>> = mutableMapOf(
            "Dungeon" to listOf(
                TilesetType.DEFAULT_DUNGEON,
                TilesetType.ROOMS_AND_CORRIDORS_B,
                TilesetType.ROOMS_LIMIT_CONNECTIVITY
            ),
            "Cave" to listOf(
                TilesetType.CAVES_LIMIT_CONNECTIVITY,
                TilesetType.CORNER_CAVES,
                TilesetType.REFERENCE_CAVES,
                TilesetType.ROUND_ROOMS_DIAGONAL_CORRIDORS,
                TilesetType.SIMPLE_CAVES
            ),
            "Mixed" to listOf(
                TilesetType.HORIZONTAL_CORRIDORS_C,
                TilesetType.ROOMS_AND_CORRIDORS_A
            ),
            "Industrial" to listOf(
                TilesetType.HORIZONTAL_CORRIDORS_A,
                TilesetType.HORIZONTAL_CORRIDORS_B,
                TilesetType.LIMITED_CONNECTIVITY,
                TilesetType.SQUARE_ROOMS_WITH_RANDOM_RECTS
            ),
            "Maze" to listOf(
                TilesetType.MAZE_A,
                TilesetType.MAZE_B,
            ),
            "Open" to listOf(
                TilesetType.OPEN_AREAS,
                TilesetType.LIMIT_CONNECTIVITY_FAT,
            )
        )
    }

    init {
        val tilesetTypeTable = VisTable()
        tilesetTypeTable.add(scene2d.visLabel("Type: ") {})
        tilesetTypeTable.add(scene2d.visLabel(formatTilesetString(tilesetType)) {
//            this@visLabel.style.background = VisTextField().style.background
            onClick { getTilesetTypeMenu(this) }
        })
        add(tilesetTypeTable).padLeft(16f).expandX().left().row()
    }

    private fun getTilesetTypeMenu(label: VisLabel) {
        val menu = PopupMenu()
        tilesetTypes.forEach { type ->
            menu.menuItem(type.key).also {
                it.subMenu()
                for (tileset in type.value) {
                    it.subMenu.menuItem(formatTilesetString(tileset)).apply { onClick { tilesetTypeChanged(tileset, label) } }
                }
            }
        }
        menu.showMenu(stage, label)
    }

    private fun tilesetTypeChanged(tilesetType: TilesetType, label: VisLabel) {
        this.tilesetType = tilesetType
        label.setText(formatTilesetString(tilesetType))
        if (label.text.length > 25)
            label.setText(label.text.substring(0, 25))

        UiManagerFactory.getUI().generateMap()
    }

    override fun getGenerationAlgorithm(params: GenerationParams): GenerationAlgorithm {
        return SquidGenerationAlgorithm(
            tilesetType,
            params,

            modifyBaseMap = modifyBaseMap
        )
    }

    private fun formatTilesetString(tilesetType: TilesetType): String {
        return tilesetType.name.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }
}
