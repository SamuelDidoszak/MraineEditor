package com.neutrino.ui.elements

import com.badlogic.gdx.graphics.g2d.Batch
import com.neutrino.textures.TextureSprite
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.util.ScalableTexture

class RulePickerButton(initTexture: TextureSprite): TextureButton(initTexture) {

    val textureList = MutableList<ScalableTexture?>(9) {null}
    private val notList = MutableList(9) {false}
    private val notTexture = ScalableTexture(Textures.get("notTexture"), width / 3, height / 3)

    var pad: Float = 2f

    override fun additionalDrawCalls(batch: Batch?, parentAlpha: Float) {
        for (y in 0 until 3) {
            for (x in 0 until 3) {
                textureList[y * 3 + x]?.draw(batch, parentAlpha,
                    this.x + x * (width / 3),
                    this.y + y * (height / 3))
                if (notList[y * 3 + x])
                    notTexture.draw(batch, parentAlpha,
                        this.x + x * (width / 3),
                        this.y + y * (height / 3))
//                if (textureList[y * 3 + x] != null)
//                    batch?.draw(textureList[y * 3 + x]?.texture?.texture,
//                        this.x + 8 + x * 40f,
//                        this.y + 8 + y * 40f)
            }
        }
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        textureList.forEach { it?.updateScale(width / 3, height / 3) }
        notTexture.updateScale(width / 3, height / 3)
    }

    fun setTopLeft(texture: TextureSprite?) {
        setTexture(6, texture)
    }

    fun setTop(texture: TextureSprite?) {
        setTexture(7, texture)
    }

    fun setTopRight(texture: TextureSprite?) {
        setTexture(8, texture)
    }

    fun setLeft(texture: TextureSprite?) {
        setTexture(3, texture)
    }

    fun setRight(texture: TextureSprite?) {
        setTexture(5, texture)
    }

    fun setBottomLeft(texture: TextureSprite?) {
        setTexture(0, texture)
    }

    fun setBottom(texture: TextureSprite?) {
        setTexture(1, texture)
    }

    fun setBottomRight(texture: TextureSprite?) {
        setTexture(2, texture)
    }

    fun setTexture(index: Int, texture: TextureSprite?) {
        if (texture == null) {
            textureList[index] = null
            return
        }
        textureList[index] = ScalableTexture(texture!!, width / 3 - pad , height / 3 + pad)
    }

    fun setTexture(index: Int, texture: ScalableTexture?) {
        if (texture == null) {
            textureList[index] = null
            return
        }
        texture.updateScale(width / 3 - pad, height / 3 - pad)
        textureList[index] = texture
    }

    fun setNot(index: Int, not: Boolean) {
        notList[index] = not
    }
}
