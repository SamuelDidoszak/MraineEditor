package com.neutrino.entities

import kotlin.random.Random


object Entities {
    private val entityIds: HashMap<String, Int> = HashMap()
    private val entityNames: ArrayList<String> = ArrayList()
    private val entityFactory: MutableList<() -> Entity> = mutableListOf()

    fun add(name: String, entity: () -> Entity) {
        entityIds[name] = entityFactory.size
        entityNames.add(name)
        entityFactory.add(entity)
    }

    fun new(name: String): Entity {
        try {
            return new(entityIds[name]!!)
        } catch (_: Exception) {
            println("Entity with name: $name does not exist!")
        }
        throw Exception()
    }

    fun new(id: Int): Entity {
        try {
            val entity = entityFactory[id].invoke()
            entity.id = id
            return entity
        } catch (_: Exception) {
            println("Entity with id: $id ${if (id < entityNames.size) "name: ${entityNames[id]} " else ""}does not exist!")
        }
        throw Exception()
    }

    fun getId(name: String): Int {
        try {
            return entityIds[name]!!
        } catch (_: Exception) {
            println("Entity with name: $name does not exist!")
        }
        throw Exception()
    }

    fun getName(id: Int): String {
        try {
            return entityNames[id]
        } catch (_: Exception) {
            println("Entity with id: $id ${if (id < entityNames.size) "name: ${entityNames[id]} " else ""}does not exist!")
        }
        throw Exception()
    }

    /**
     * Picks one texture name from provided with and equal probability
     * Returns null if $to value was higher than value
     */
    fun getRandomTexture(randomValue: Float, until: Float = 100f, textures: List<String>): String? {
        val increment = until / textures.size
        var max = increment
        for (texture in textures) {
            if (randomValue < max)
                return texture
            max += increment
        }
        return null
    }

    /**
     * Picks one texture name from provided with and equal probability
     * Returns the first texture if wrong data was provided
     */
    fun getRandomTexture(random: Random, texturesPercent: List<Pair<Float, List<String>>>): String? {
        val texturesPercent = texturesPercent.sortedBy { it.first }
        val randVal = random.nextFloat() * 100f
        var texture: String? = null
        var from = 0f
        for (textureMap in texturesPercent) {
            val step = textureMap.first / textureMap.second.size
            if (randVal <= from + textureMap.first) {
                texture = textureMap.second[((randVal - from) / step).toInt()]
                break
            }

            from += textureMap.first
        }
        return texture
    }
}
