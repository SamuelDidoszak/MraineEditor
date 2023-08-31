package com.neutrino.ui.editor

import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.generation.Generators
import com.neutrino.generation.MapTagInterpretation
import com.neutrino.generation.Tilesets
import com.neutrino.generation.util.GenerationParams
import com.neutrino.textures.LevelDrawer
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class Editor: Group() {

    private val levelDrawer = LevelDrawer()
    val editorStage = EditorStage(levelDrawer)
    val editorGeneration = EditorGeneration(levelDrawer)

    init {
        editorStage.addActor(levelDrawer)

        val params = GenerationParams(MapTagInterpretation(listOf()), Random(2137), levelDrawer.map)
        params.interpretedTags.tilesets.add(Tilesets.get("Dungeon"))

        val generationTimes = ArrayList<Long>(40)
        val texturingTimes = ArrayList<Long>(40)

        for (i in 0 until 1) {
            levelDrawer.clearAll()
            levelDrawer.map = levelDrawer.initializeMap()
            params.map = levelDrawer.map
            generationTimes.add(measureTimeMillis {
                Generators.get("Test").generate(params)
            })
            texturingTimes.add(measureTimeMillis {
                levelDrawer.initializeTextures()
            })
        }
        for (i in 0 until generationTimes.size) {
            println("${generationTimes[i]}, ${texturingTimes[i]}: ${generationTimes[i] + texturingTimes[i]}")
        }
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
