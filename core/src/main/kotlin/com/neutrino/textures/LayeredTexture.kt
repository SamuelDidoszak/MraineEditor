package com.neutrino.textures

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.OnMapPositionAttribute
import com.neutrino.util.Constants.SCALE_INT

internal data class LayeredTexture(
    val entity: Entity,
    val texture: TextureSprite
) {
    private val positionAttribute = entity.get(OnMapPositionAttribute::class)!!

    /** Returns scaled x position including map placement */
    fun getX(): Float {
        return positionAttribute.x * 16 * SCALE_INT + texture.x * SCALE_INT
    }

    /** Returns scaled y position including map placement */
    fun getY(): Float {
        return positionAttribute.y * SCALE_INT + texture.y * SCALE_INT
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
