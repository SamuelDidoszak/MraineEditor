package com.neutrino.textures

import com.neutrino.entities.Entity

internal class LayeredTextureUnsorted(
    entity: Entity,
    texture: TextureSprite
): LayeredTexture(entity, texture) {

    override fun getYSort(): Float {
        return getYPos()
    }
}
