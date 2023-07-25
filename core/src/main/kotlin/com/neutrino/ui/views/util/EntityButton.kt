package com.neutrino.ui.views.util

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.entities.Entity
import com.neutrino.textures.SingleEntityDrawer
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.TextureButton

class EntityButton(entity: Entity): Group() {

    private var entityDrawer = SingleEntityDrawer(entity)
    private val textureButton = object : TextureButton(Textures.get("emptyTexture")) {

        override fun additionalDrawCalls(batch: Batch?, parentAlpha: Float) {
            entityDrawer.draw(batch, parentAlpha)
        }
    }

    init {
        addActor(textureButton)
        addActor(entityDrawer)
        entityDrawer.isVisible = false
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        textureButton.setSize(width, height)
        entityDrawer.setSize(width, height)
    }
}
