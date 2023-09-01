package com.neutrino.textures

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.PositionAttribute
import com.neutrino.util.Constants.SCALE_INT

internal open class LayeredTexture(
    val entity: Entity,
    val texture: TextureSprite
) {
    private val positionAttribute = entity.get(PositionAttribute::class)!!

    /** Returns scaled x position including map placement */
    fun getX(): Float {
        return positionAttribute.x + texture.x * SCALE_INT * if (texture.mirrorX) -1 else 1
    }

    /** Returns scaled y position including map placement */
    fun getY(): Float {
        return positionAttribute.y + texture.y * SCALE_INT
    }

    fun getYPos(): Float {
        return positionAttribute.y
    }

    open fun getYSort(): Float {
        return getY()
    }

    /** Returns scaled width */
    fun getWidth(): Int {
        return texture.texture.regionWidth * SCALE_INT
    }

    /** Returns scaled height */
    fun getHeight(): Int {
        return texture.texture.regionHeight * SCALE_INT
    }

    operator fun compareTo(value: LayeredTexture): Int {
        return compareValues(getY(), value.getY())
    }
}
