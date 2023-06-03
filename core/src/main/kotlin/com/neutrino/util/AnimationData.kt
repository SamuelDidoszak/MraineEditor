package com.neutrino.util

import com.neutrino.entities.Entity

data class AnimationData(
    val animation: AnimatedTextureSprite,
    val entity: Entity,
    val nextAnimation: AnimatedTextureSprite? = null
)
