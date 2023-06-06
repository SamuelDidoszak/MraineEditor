package com.neutrino.textures

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.OnMapPositionAttribute
import java.util.*

class LevelDrawer {

    val animations: Animations = Animations()
    val lights: ArrayList<Pair<Entity, Light>> = ArrayList()
    private val textureLayers: SortedMap<Int, ArrayList<Pair<Entity, TextureSprite>>> = sortedMapOf()

    fun addTexture(entity: Entity, texture: TextureSprite) {
        if (textureLayers[texture.z] == null)
            textureLayers[texture.z] = ArrayList()
        textureLayers[texture.z]!!.add(Pair(entity, texture))
    }

    fun removeTexture(entity: Entity, texture: TextureSprite) {
        textureLayers[texture.z]!!.removeIf { it.first == entity && it.second == texture }
    }

    lateinit var map: List<List<MutableList<Entity>>>

    fun initializeTextures() {
        for (y in map.indices) {
            for (x in map[0].indices) {
                for (entity in map[y][x]) {
                    entity addAttribute OnMapPositionAttribute(x, y, this)
                }
            }
        }
    }

    fun initializeMap(): List<List<MutableList<Entity>>> {
        return List(100) {
            List(100) {
                ArrayList<Entity>()
            }
        }
    }
}
