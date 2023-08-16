package com.neutrino.textures

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import kotlin.random.Random

object Textures {
    val atlases: HashMap<String, TextureAtlas> = HashMap()
    private val textures: HashMap<String, TextureSprite> = HashMap()

    infix fun add(textureSprite: TextureSprite) {
        if (textures[textureSprite.texture.name] != null)
            throw Exception("Texture name already exists!")
        textures[textureSprite.texture.name] = textureSprite
    }

    infix fun add(textureSprite: AnimatedTextureSprite) {
        val name = textureSprite.texture.name.substringBefore('#')
        if (textures[name] != null)
            throw Exception("Texture name already exists!")
        textures[name] = textureSprite
    }

    infix fun get(name: String): TextureSprite {
        return textures[name]!!
    }

    infix fun getOrNull(name: String?): TextureSprite? {
        if (name == null)
            return null
        return textures[name]
    }

    fun getOrNull(random: Random, probability: Float, texture: String): TextureSprite? {
        if (random.nextFloat() * 100f <= probability)
            return get(texture)
        return null
    }

    /**
     * Picks one texture name from provided with and equal probability
     * Returns null if $to value was higher than value
     */
    fun getRandomTexture(random: Random, until: Float = 100f, textures: List<String>): TextureSprite? {
        val randVal = random.nextFloat() * 100f
        val increment = until / textures.size
        var max = increment
        for (texture in textures) {
            if (randVal < max)
                return get(texture)
            max += increment
        }
        return null
    }

    /**
     * Picks one texture name from provided with and equal probability
     * Returns the first texture if wrong data was provided
     */
    fun getRandomTexture(random: Random, texturesPercent: List<Pair<Float, List<String>>>): TextureSprite? {
        val texturesPercent = texturesPercent.sortedBy { it.first }
        val randVal = random.nextFloat() * 100f
        var texture: String? = null
        var from = 0f
        for (textureMap in texturesPercent) {
            val step = textureMap.first / textureMap.second.size
            if (randVal <= from + textureMap.first) {
                texture = textureMap.second[((randVal - from) / step).toInt()]
                break
            }

            from += textureMap.first
        }
        return getOrNull(texture)
    }
}
