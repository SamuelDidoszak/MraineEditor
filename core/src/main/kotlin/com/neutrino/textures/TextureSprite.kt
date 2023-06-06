package com.neutrino.textures

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import kotlin.random.Random

open class TextureSprite(
    var texture: AtlasRegion,
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Int = 1
) {
    constructor(
        texture: AtlasRegion,
        lights: LightSources,
        x: Float = 0f,
        y: Float = 0f,
        z: Int = 1): this(texture, x, y, z) {
        this.lights = lights
    }

    var lights: LightSources? = null
    var mirrored: Boolean = false

    fun mirror() {
        mirrored = !mirrored
    }
    fun mirror(probability: Float, randomGenerator: Random) {
        if (randomGenerator.nextFloat() * 100 < probability)
            mirrored = !mirrored
    }
}
