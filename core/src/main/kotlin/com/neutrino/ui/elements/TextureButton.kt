package com.neutrino.ui.elements

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Pools
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.TextureSprite
import com.neutrino.util.Constants
import kotlin.math.min

class TextureButton(initTexture: TextureSprite): Actor() {

    var texture: TextureSprite = initTexture
        set(value) {
            field = value
            updateScale()
        }
    private val textureAttributes = TextureAttributes()

    private var checked = false
    var disabled = false
        set(value) {
            if (value)
                setChecked(false, false)
            field = value
        }

    private val clickListener = object : ClickListener() {
        override fun clicked(event: InputEvent, x: Float, y: Float) {
            if (disabled) return
            setChecked(!checked, true)
        }
    }

    private companion object {
        var COLOR_DOWN: Color = Color.BLACK
        var COLOR_OVER: Color = Color.GRAY
        var COLOR_DISABLED: Color = Color.PURPLE
    }
    private var backgroundColor: Color? = null

    fun setBackgroundColor(color: Color = Color(51 / 255f, 51 / 255f, 51 / 255f, 1f)) {
        backgroundColor = color
    }

    init {
        touchable = Touchable.enabled
        addListener(clickListener)
    }

    fun setChecked(isChecked: Boolean, fireEvent: Boolean) {
        if (checked == isChecked) return
        checked = isChecked
        if (fireEvent) {
            val changeEvent = Pools.obtain(
                ChangeListener.ChangeEvent::class.java
            )
            if (fire(changeEvent)) checked = !isChecked
            Pools.free(changeEvent)
        }
    }

    private fun getOverlayingColor(): Color {
        if (disabled)
            return COLOR_DISABLED
        if (clickListener.isPressed)
            return COLOR_DOWN
        if (clickListener.isOver)
            return COLOR_OVER
        if (checked && clickListener.isOver)
            return COLOR_OVER
        return Color.WHITE
    }

    override fun act(delta: Float) {
        (texture as? AnimatedTextureSprite)?.setFrame(delta)
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (backgroundColor != null) {
            val overlayColor = Color(getOverlayingColor())
            batch?.color =
                if (overlayColor != Color.WHITE)
                    overlayColor.lerp(backgroundColor, 0.5f)
                else
                    backgroundColor
            batch?.draw(Constants.whitePixel, x, y, width, height)
        }
        batch?.color = getOverlayingColor()
        batch?.draw(texture.texture, x + textureAttributes.offsetX, y + textureAttributes.offsetY,
            textureAttributes.width, textureAttributes.height)
        batch?.color = Color.WHITE
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        updateScale()
    }

    private fun updateScale() {
        val wScale: Float = width / texture.texture.regionWidth
        val hScale: Float = height / texture.texture.regionHeight
        val scale = min(wScale, hScale)
        textureAttributes.width = texture.width() * scale
        textureAttributes.height = texture.height() * scale
        textureAttributes.offsetX = (width - textureAttributes.width) / 2
        textureAttributes.offsetY = (height - textureAttributes.height) / 2
    }

    private data class TextureAttributes(
        var width: Float = 0f,
        var height: Float = 0f,
        var offsetX: Float = 0f,
        var offsetY: Float = 0f
    )
}



















