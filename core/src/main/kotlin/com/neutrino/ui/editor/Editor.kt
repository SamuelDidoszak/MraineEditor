package com.neutrino.ui.editor

import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.generation.Generators
import com.neutrino.generation.MapTagInterpretation
import com.neutrino.generation.Tilesets
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
        Generators.add("Test") {
            SquidGenerationAlgorithm(TilesetType.DEFAULT_DUNGEON, it).generateAll(Tilesets.get("Dungeon"))
        }

        val params = GenerationParams(MapTagInterpretation(listOf()), Random(2137), levelDrawer.map)
        Generators.get("Test").generate(params)

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
