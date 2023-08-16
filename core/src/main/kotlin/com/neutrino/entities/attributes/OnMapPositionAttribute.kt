package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.Entity
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.EntityDrawer
import com.neutrino.textures.TextureSprite
import kotlin.reflect.KClass

class OnMapPositionAttribute(
    var x: Int,
    var y: Int,
    var level: EntityDrawer
): Attribute() {
    fun getMap(): List<List<MutableList<Entity>>> {
        return level.map
    }

    private companion object {
        val positionMap = mapOf(
            1 to (-1 to -1),
            2 to (0 to -1),
            3 to (1 to -1),
            4 to (-1 to 0),
            5 to (0 to 0),
            6 to (1 to 0),
            7 to (-1 to 1),
            8 to (0 to 1),
            9 to (1 to 1)
        )

        val mirrorXMap = mapOf(
            1 to 3,
            2 to 2,
            3 to 1,
            4 to 6,
            5 to 5,
            6 to 4,
            7 to 9,
            8 to 8,
            9 to 7
        )

        val mirrorYMap = mapOf(
            1 to 7,
            2 to 8,
            3 to 9,
            4 to 4,
            5 to 5,
            6 to 6,
            7 to 1,
            8 to 2,
            9 to 3
        )
    }

    fun check(position: List<Int>, name: String, not: Boolean = false, mirrorX: Boolean = false, mirrorY: Boolean = false, unit: () -> TextureSprite?): TextureSprite? {
        return check(position, NameOrIdentity(name, not), unit)
    }

    fun check(position: List<Int>, identity: KClass<out Identity>, not: Boolean = false, mirrorX: Boolean = false, mirrorY: Boolean = false,  unit: () -> TextureSprite?): TextureSprite? {
        return check(position, NameOrIdentity(identity, not), mirrorX, mirrorY, unit)
    }

    fun check(position: List<Int>, nameOrIdentity: NameOrIdentity, mirrorX: Boolean, mirrorY: Boolean, unit: () -> TextureSprite?): TextureSprite? {
        var textureSprite: TextureSprite? = check(position, nameOrIdentity, unit)
        if (textureSprite != null)
            return textureSprite

        if (mirrorX) {
            textureSprite = check(position.map {mirrorXMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorX()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorY) {
            textureSprite = check(position.map {mirrorYMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorY()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorX && mirrorY) {
            textureSprite = check(position.map {mirrorXMap[mirrorYMap[it]!!]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorX()?.mirrorY()

            if (textureSprite != null)
                return textureSprite
        }
        return null
    }

    fun check(position: List<Int>, nameOrIdentity: NameOrIdentity, unit: () -> TextureSprite?): TextureSprite? {
        for (i in position.indices) {
            val xy = positionMap[position[i]]!!
            val x = x + xy.first
            val y = y + xy.second
            if (y !in level.map.indices || x !in level.map[0].indices && !nameOrIdentity.not)
                return null
            else for (entity in level.map[y][x]) {
                if (!nameOrIdentity.isSame(entity))
                    return null
            }
        }
        return unit.invoke()
    }

    fun check(requirements: List<Pair<Int, NameOrIdentity>>, mirrorX: Boolean, mirrorY: Boolean, unit: () -> TextureSprite?): TextureSprite? {
        var textureSprite: TextureSprite? = check(requirements, unit)
        if (textureSprite != null)
            return textureSprite

        if (mirrorX) {
            textureSprite = check(requirements.map { mirrorXMap[it.first]!! to it.second }, unit)
            textureSprite?.mirrorX()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorY) {
            textureSprite = check(requirements.map { mirrorYMap[it.first]!! to it.second }, unit)
            textureSprite?.mirrorY()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorX && mirrorY) {
            textureSprite = check(requirements.map { mirrorXMap[mirrorYMap[it.first]!!]!! to it.second }, unit)
            textureSprite?.mirrorX()?.mirrorY()

            if (textureSprite != null)
                return textureSprite
        }
        return null
    }

    fun check(requirements: List<Pair<Int, NameOrIdentity>>, unit: () -> TextureSprite?): TextureSprite? {
        for (i in requirements.indices) {
            val xy = positionMap[requirements[i].first]!!
            val x = x + xy.first
            val y = y + xy.second
            if (y !in level.map.indices || x !in level.map[0].indices && !requirements[i].second.not)
                return null
            else for (entity in level.map[y][x]) {
                if (!requirements[i].second.isSame(entity))
                    return null
            }
        }
        return unit.invoke()
    }
}
