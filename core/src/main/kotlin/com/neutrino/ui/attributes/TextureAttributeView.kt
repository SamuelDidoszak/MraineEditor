package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.TextureSprite
import com.neutrino.ui.elements.TextureButton
import com.neutrino.util.set

class TextureAttributeView: AttributeView(VisTable()) {

    override val attributeName = "TextureAttribute"
    private val addImage = TextureSprite(TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))
    private var textureView = TextureButton(addImage)

    init {
        TableUtils.setSpacingDefaults(table)

        textureView.setSize(96f, 96f)
        textureView.setBackgroundColor()
        table.add(textureView).left()
        textureView.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                textureView.texture = animatedSprite
            }
        })
    }

    val animatedSprite = AnimatedTextureSprite(
        Array<TextureAtlas.AtlasRegion>()
            .set(TextureAtlas.AtlasRegion(
                Texture(Gdx.files.internal("standingTorch$2#1.png")), 0, 0, 16, 32))
            .set(TextureAtlas.AtlasRegion(
                Texture(Gdx.files.internal("standingTorch$2#2.png")), 0, 0, 16, 32))
            .set(TextureAtlas.AtlasRegion(
                Texture(Gdx.files.internal("standingTorch$2#3.png")), 0, 0, 16, 32))
    )
}








