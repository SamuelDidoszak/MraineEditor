package com.neutrino.ui.editor

import com.neutrino.entities.Entity
import com.neutrino.generation.MapTag
import com.neutrino.generation.MapTagInterpretation
import com.neutrino.generation.util.GenerationParams
import com.neutrino.textures.LevelDrawer
import kotlin.random.Random

class EditorGeneration(var levelDrawer: LevelDrawer) {

    private var tags: ArrayList<MapTag> = ArrayList()
    private var tagGenerators: ArrayList<() -> MapTag> = ArrayList()
    var seed: Long = 2137
    var mapXSize = 100
    var mapYSize = 100
    var map: List<List<MutableList<Entity>>> = clearMap()

    fun generateMap() {
        clearLevelDrawer()
        val params = getParams()
        for (generator in params.interpretedTags.mapGenerators) {
            generator.generate(params)
        }
        levelDrawer.initializeTextures(params.rng)
    }

    private fun getParams(): GenerationParams {
        return GenerationParams(
            MapTagInterpretation(
                tagGenerators.map { it.invoke() }
            ),
            Random(seed),
            levelDrawer.map
        )
    }

    private fun clearLevelDrawer() {
        levelDrawer.clearAll()
        levelDrawer.map = levelDrawer.initializeMap()
    }

    fun registerTag(tag: MapTag) {
        tags.add(tag)
    }

    fun registerTag(tag: () -> MapTag) {
        tagGenerators.add(tag)
    }

    fun unregisterTag(tag: MapTag) {
        tags.remove(tag)
    }

    fun unregisterTag(tag: () -> MapTag) {
        tagGenerators.remove(tag)
    }

    private fun clearMap(): List<List<MutableList<Entity>>> {
        return List(mapXSize) {
            List(mapYSize) {
                java.util.ArrayList<Entity>()
            }
        }
    }
}
