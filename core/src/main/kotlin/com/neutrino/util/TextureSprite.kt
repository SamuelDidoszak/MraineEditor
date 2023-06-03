package com.neutrino.util

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import kotlin.random.Random

open class TextureSprite(
    var texture: TextureAtlas.AtlasRegion,
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var mirrored: Boolean = false
) {
    fun mirror() {
        mirrored = !mirrored
    }
    fun mirror(probability: Float, randomGenerator: Random) {
        if (randomGenerator.nextFloat() * 100 < probability)
            mirrored = !mirrored
    }
}
