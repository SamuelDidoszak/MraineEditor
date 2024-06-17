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
import com.neutrino.ui.elements.util.ScalableTexture
import com.neutrino.util.Constants

open class TextureButton(initTexture: TextureSprite, var canBeActivated: Boolean = false): Actor() {

    protected var scalableTexture: ScalableTexture = ScalableTexture(initTexture, width, height)

    var texture: TextureSprite
        set(value) {scalableTexture.texture = value}
        get() = scalableTexture.texture

    var centered = true
        set(value) {
            field = value
            scalableTexture.centered = value
        }
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
        var COLOR_DOWN: Color = Color.DARK_GRAY
        var COLOR_OVER: Color = Color.GRAY
        var COLOR_DISABLED: Color = Color.PURPLE
        var COLOR_ACTIVE: Color = Color.ROYAL
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
        if (checked && canBeActivated)
            return COLOR_ACTIVE
        return Color.WHITE
    }

    override fun act(delta: Float) {
        (texture as? AnimatedTextureSprite)?.setFrame(delta)
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        drawBackgroundColor(batch)
        drawTexture(batch)
        additionalDrawCalls(batch, parentAlpha)
        batch?.color = Color.WHITE
    }

    protected fun drawBackgroundColor(batch: Batch?) {
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
    }

    protected fun drawTexture(batch: Batch?) {
        batch?.draw(texture.texture,
            x + scalableTexture.offsetX + texture.x.positiveOrZero(scalableTexture.offsetX) * scalableTexture.scale,
            y + scalableTexture.offsetY + texture.y.positiveOrZero(scalableTexture.offsetY) * scalableTexture.scale,
            scalableTexture.texture.width() * scalableTexture.scale,
            scalableTexture.texture.height() * scalableTexture.scale)
    }

    open fun additionalDrawCalls(batch: Batch?, parentAlpha: Float) {}

    private fun Float.positiveOrZero(offset: Float): Float = if (this + offset >= 0f) this else 0f

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        scalableTexture.updateScale(width, height)
    }
}



















