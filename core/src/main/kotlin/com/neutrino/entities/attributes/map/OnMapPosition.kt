package com.neutrino.entities.attributes.map

import attributes.Attribute
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Identity
import com.neutrino.entities.attributes.Position
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.EntityDrawer
import com.neutrino.textures.TextureSprite
import com.neutrino.util.Constants.SCALE
import com.neutrino.util.Constants.SCALE_INT
import com.neutrino.util.add
import kotlin.reflect.KClass

class OnMapPosition(
    x: Int,
    y: Int,
    var level: EntityDrawer
): Attribute() {

    var x: Int = x
        set(value) {
            field = value
            entity.get(Position::class)!!.x = value * 16 * SCALE
        }

    var y: Int = y
        set(value) {
            field = value
            entity.get(Position::class)!!.y =
                level.map.size * 16 * SCALE_INT - value * 16 * SCALE
        }

    override fun onEntityAttached() {
        this.x = x
        this.y = y
    }

    fun getMap(): List<List<MutableList<Entity>>> {
        return level.map
    }

    private companion object {
        val positionMap = mapOf(
            1 to (-1 to 1),
            2 to (0 to 1),
            3 to (1 to 1),
            4 to (-1 to 0),
            5 to (0 to 0),
            6 to (1 to 0),
            7 to (-1 to -1),
            8 to (0 to -1),
            9 to (1 to -1)
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
        return check(position, NameOrIdentity(name, not), mirrorX, mirrorY, unit)
    }

    fun check(position: List<Int>, identity: KClass<out Identity>, not: Boolean = false, mirrorX: Boolean = false, mirrorY: Boolean = false, unit: () -> TextureSprite?): TextureSprite? {
        return check(position, NameOrIdentity(identity, not), mirrorX, mirrorY, unit)
    }

    fun check(position: List<Int>, nameOrIdentity: NameOrIdentity, mirrorX: Boolean, mirrorY: Boolean, unit: () -> TextureSprite?): TextureSprite? {
        var textureSprite: TextureSprite? = check(position, nameOrIdentity, unit)
        if (textureSprite != null)
            return textureSprite

        if (mirrorX) {
            textureSprite = check(position.map { mirrorXMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorX()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorY) {
            textureSprite = check(position.map { mirrorYMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorY()

            if (textureSprite != null)
                return textureSprite
        }
        if (mirrorX && mirrorY) {
            textureSprite = check(position.map { mirrorXMap[mirrorYMap[it]!!]!!}, nameOrIdentity, unit)
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
            if (y !in level.map.indices || x !in level.map[0].indices) {
                if (nameOrIdentity.not)
                    continue
                return null
            }
            var add = false
            for (entity in level.map[y][x]) {
                if (nameOrIdentity.isSame(entity))
                    add = true
                else if (nameOrIdentity.not)
                    return null
            }
            if (!add)
                return null
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
            if (y !in level.map.indices || x !in level.map[0].indices) {
                if (requirements[i].second.not)
                    continue
                return null
            }
            var add = false
            for (entity in level.map[y][x]) {
                if (requirements[i].second.isSame(entity))
                    add = true
                else if (requirements[i].second.not)
                    return null
            }
            if (!add)
                return null
        }
        return unit.invoke()
    }

    fun check(position: List<Int>, name: String, not: Boolean = false, mirrorX: Boolean, mirrorY: Boolean, checkAll: Boolean, unit: () -> TextureSprite?): List<TextureSprite>? {
        return check(position, NameOrIdentity(name, not), mirrorX, mirrorY, checkAll, unit)
    }

    fun check(position: List<Int>, identity: KClass<out Identity>, not: Boolean = false, mirrorX: Boolean, mirrorY: Boolean, checkAll: Boolean, unit: () -> TextureSprite?): List<TextureSprite>? {
        return check(position, NameOrIdentity(identity, not), mirrorX, mirrorY, checkAll, unit)
    }

    fun check(position: List<Int>, nameOrIdentity: NameOrIdentity, mirrorX: Boolean, mirrorY: Boolean, checkAll: Boolean, unit: () -> TextureSprite?): List<TextureSprite>? {
        val textureSprites = ArrayList<TextureSprite>()
        var textureSprite: TextureSprite? = check(position, nameOrIdentity, unit)
        textureSprites.add(textureSprite)
        if (!checkAll && textureSprite != null)
            return textureSprites

        if (mirrorX) {
            textureSprite = check(position.map { mirrorXMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorX()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }
        if (mirrorY) {
            textureSprite = check(position.map { mirrorYMap[it]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorY()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }
        if (mirrorX && mirrorY) {
            textureSprite = check(position.map { mirrorXMap[mirrorYMap[it]!!]!!}, nameOrIdentity, unit)
            textureSprite?.mirrorX()?.mirrorY()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }

        if (textureSprites.isEmpty())
            return null
        return textureSprites
    }

    fun check(requirements: List<Pair<Int, NameOrIdentity>>, mirrorX: Boolean, mirrorY: Boolean, checkAll: Boolean, unit: () -> TextureSprite?): List<TextureSprite>? {
        val textureSprites = ArrayList<TextureSprite>()
        var textureSprite: TextureSprite? = check(requirements, unit)
        textureSprites.add(textureSprite)
        if (!checkAll && textureSprite != null)
            return textureSprites

        if (mirrorX) {
            textureSprite = check(requirements.map { mirrorXMap[it.first]!! to it.second }, unit)
            textureSprite?.mirrorX()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }
        if (mirrorY) {
            textureSprite = check(requirements.map { mirrorYMap[it.first]!! to it.second }, unit)
            textureSprite?.mirrorY()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }
        if (mirrorX && mirrorY) {
            textureSprite = check(requirements.map { mirrorXMap[mirrorYMap[it.first]!!]!! to it.second }, unit)
            textureSprite?.mirrorX()?.mirrorY()
            textureSprites.add(textureSprite)
            if (!checkAll && textureSprite != null)
                return textureSprites
        }

        if (textureSprites.isEmpty())
            return null
        return textureSprites
    }
}
