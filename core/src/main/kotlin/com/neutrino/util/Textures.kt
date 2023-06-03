package com.neutrino.util

object Textures {
    private val textures: HashMap<String, TextureSprite> = HashMap()

    fun add(textureSprite: TextureSprite) {
        if (textures[textureSprite.texture.name] != null)
            throw Exception("Texture name already exists!")
        textures[textureSprite.texture.name] = textureSprite
    }

    fun get(name: String): TextureSprite {
        return textures[name]!!
    }
}
