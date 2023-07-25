package com.neutrino.ui.elements.util

import com.badlogic.gdx.graphics.g2d.Batch
import com.neutrino.textures.TextureSprite
import kotlin.math.abs
import kotlin.math.min

class ScalableTexture(
    texture: TextureSprite,
    var maxWidth: Float,
    var maxHeight: Float,
    var centered: Boolean = true,
    var scale: Float = 1f,
    var width: Float = 0f,
    var height: Float = 0f,
    var offsetX: Float = 0f,
    var offsetY: Float = 0f
) {

    var texture: TextureSprite = texture
        set(value) {
            field = value
            updateScale(maxWidth, maxHeight)
        }

    init {
        updateScale(maxWidth, maxHeight)
    }

    fun updateScale(width: Float = maxWidth, height: Float = maxHeight) {
        if (width != maxWidth)
            maxWidth = width
        if (height != maxHeight)
            maxHeight = height

        val wScale: Float = maxWidth / (texture.texture.regionWidth + abs(texture.x) * if(centered) 2 else 1)
        val hScale: Float = maxHeight / (texture.texture.regionHeight + abs(texture.y) * if(centered) 2 else 1)
        val scale = min(wScale, hScale)
        this.scale = scale
        this.width = texture.width() * scale
        this.height = texture.height() * scale

        if (!centered) {
            this.offsetX = 0f
            this.offsetY = 0f
            return
        }

        this.offsetX = (width - this.width) / 2
        this.offsetY = (height - this.height) / 2
    }

    fun draw(batch: Batch?, parentAlpha: Float, x: Float, y: Float) {
        batch?.draw(texture.texture,
            x + offsetX + texture.x.positiveOrZero(offsetX) * scale,
            y + offsetY + texture.y.positiveOrZero(offsetY) * scale,
            width, height)
    }

    private fun Float.positiveOrZero(offset: Float): Float = if (this + offset >= 0f) this else 0f
}
