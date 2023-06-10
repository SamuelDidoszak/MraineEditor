package com.neutrino.ui.elements

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.TextureSprite

class TextureImage(var textureSprite: TextureSprite): Actor() {

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.draw(textureSprite.texture, textureSprite.x, textureSprite.y)
    }

    override fun act(delta: Float) {
        (textureSprite as? AnimatedTextureSprite)?.setFrame(delta)
    }
}
