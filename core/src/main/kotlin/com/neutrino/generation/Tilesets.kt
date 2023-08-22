package com.neutrino.generation

import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityName

object Tilesets {

    private val tilesetMap: HashMap<String, Tileset> = HashMap()

    fun add(name: String, tileset: Tileset): Tileset {
        tilesetMap[name] = tileset
        return tileset
    }

    fun add(name: String, tileset: List<Pair<Identity, EntityName>>): Tileset {
        tilesetMap[name] = Tileset(tileset)
        return tilesetMap[name]!!
    }

    fun get(name: String): Tileset {
        return tilesetMap[name]!!
    }

    fun getAllTilesets(): HashMap<String, Tileset> {
        return tilesetMap
    }

    fun getAllTilesetNames(): Set<String> {
        return tilesetMap.keys
    }
}
