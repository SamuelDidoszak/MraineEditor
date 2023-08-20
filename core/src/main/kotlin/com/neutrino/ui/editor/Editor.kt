package com.neutrino.ui.editor

import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.MapTagInterpretation
import com.neutrino.generation.Tileset
import com.neutrino.generation.algorithms.SquidGenerationAlgorithm
import com.neutrino.generation.util.GenerationParams
import com.neutrino.textures.LevelDrawer
import squidpony.squidgrid.mapping.styled.TilesetType
import kotlin.random.Random

class Editor: Group() {

    private val levelDrawer = LevelDrawer()
    val editorStage = EditorStage(levelDrawer)

    init {
        editorStage.addActor(levelDrawer)
        val params = GenerationParams(MapTagInterpretation(listOf()), Random(2137), levelDrawer.map)
        levelDrawer.map = SquidGenerationAlgorithm(TilesetType.DEFAULT_DUNGEON, params).generate(Tileset(listOf(
            Identity.Wall() to "DungeonWall",
            Identity.Floor() to "DungeonFloorClean"
        )))

        levelDrawer.initializeTextures()
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        editorStage.viewport.setScreenSize(width.toInt(), height.toInt())
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        editorStage.viewport.setScreenPosition(x.toInt(), y.toInt())
        editorStage.setPosition(x.toInt(), y.toInt())
    }
}
