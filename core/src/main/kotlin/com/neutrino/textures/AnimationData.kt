package com.neutrino.textures

import com.neutrino.entities.Entity

data class AnimationData(
    val animation: AnimatedTextureSprite,
    val entity: Entity,
    val nextAnimation: AnimatedTextureSprite? = null
) {
    override fun equals(other: Any?): Boolean {
        return other is AnimationData && entity == other.entity && animation == other.animation
    }

    override fun hashCode(): Int {
        var result = animation.hashCode()
        result = 31 * result + entity.hashCode()
        result = 31 * result + (nextAnimation?.hashCode() ?: 0)
        return result
    }
}
