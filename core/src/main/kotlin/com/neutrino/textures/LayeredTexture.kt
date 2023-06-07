package com.neutrino.textures

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.OnMapPositionAttribute

internal data class LayeredTexture(
    val entity: Entity,
    val texture: TextureSprite
) {
    private val positionAttribute = entity.get(OnMapPositionAttribute::class)!!

    fun getX(): Float {
        return positionAttribute.x + texture.x
    }

    fun getY(): Float {
        return positionAttribute.y + texture.y
    }

    fun getWidth(): Int {
        return texture.texture.regionWidth
    }

    fun getHeight(): Int {
        return texture.texture.regionHeight
    }

    operator fun compareTo(value: LayeredTexture): Int {
        return compareValues(getY(), value.getY())
    }
}
