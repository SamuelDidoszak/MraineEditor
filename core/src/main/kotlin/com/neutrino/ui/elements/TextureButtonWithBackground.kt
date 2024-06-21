package com.neutrino.ui.elements

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.neutrino.textures.TextureSprite
import com.neutrino.textures.Textures
import com.neutrino.util.compareDelta
import com.neutrino.util.equalsDelta
import kotlin.math.absoluteValue

class TextureButtonWithBackground(
    initTexture: TextureSprite,
    val character: Boolean = false,
    backgroundImage: TextureSprite? = null,
    canBeActivated: Boolean = false
): TextureButton(initTexture, canBeActivated) {

    private val backgroundImage = backgroundImage ?: if (character) Textures.get("textureBg32") else Textures.get("textureBg16")

    override fun draw(batch: Batch?, parentAlpha: Float) {
        drawBackgroundColor(batch)
        drawBackgroundImage(batch)
        drawTexture(batch)
        additionalDrawCalls(batch, parentAlpha)
        batch?.color = Color.WHITE
    }

    private fun drawBackgroundImage(batch: Batch?) {
        if (scalableTexture.texture.x.equalsDelta(0f) &&
            scalableTexture.texture.y.equalsDelta(0f) &&
            scalableTexture.texture.width() < (if (character) 32f else 16f) &&
            scalableTexture.texture.height() < (if (character) 32f else 16f))
            return

        batch?.draw(backgroundImage.texture,
            x + texture.x.negativeOrZero() * scalableTexture.scale,
            y + texture.y.negativeOrZero() * scalableTexture.scale,
            backgroundImage.texture.regionWidth.toFloat() * scalableTexture.scale,
            backgroundImage.texture.regionHeight.toFloat() * scalableTexture.scale)
    }

    private fun Float.negativeOrZero(): Float {
        if (this.compareDelta(0f) == -1)
            return this.absoluteValue
        else
            return 0f
    }
}
