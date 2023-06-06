package com.neutrino.textures

import com.badlogic.gdx.graphics.g2d.TextureAtlas

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
}
