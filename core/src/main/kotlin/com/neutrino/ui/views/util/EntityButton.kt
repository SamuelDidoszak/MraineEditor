package com.neutrino.ui.views.util

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.building.utilities.Alignment
import com.kotcrab.vis.ui.widget.VisLabel
import com.neutrino.entities.Entity
import com.neutrino.textures.SingleEntityDrawer
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.TextureButton
import com.neutrino.util.Constants

class EntityButton(entity: Entity, private val x1Size: Boolean = false): Group() {

    private var entityDrawer = SingleEntityDrawer(entity)
    private var entityNameLabel = VisLabel(
        addSpaceBeforeUppercase(entity.name),
        if (x1Size)
            Constants.VisUIX1Skin.get(LabelStyle::class.java)
        else
            VisUI.getSkin().get(LabelStyle::class.java)
    )
    private val textureButton = object : TextureButton(Textures.get("emptyTexture")) {

        override fun additionalDrawCalls(batch: Batch?, parentAlpha: Float) {
            entityDrawer.draw(batch, parentAlpha)
        }
    }

    private fun addSpaceBeforeUppercase(string: String): String {
        var newString = ""
        for (i in string.indices) {
            if (string[i].isUpperCase())
                newString += ' '
            newString += string[i]
        }
        return newString
    }

    init {
        addActor(textureButton)
        addActor(entityDrawer)
        entityDrawer.isVisible = false
        addActor(entityNameLabel)
        entityNameLabel.wrap = true
        entityNameLabel.setAlignment(Alignment.TOP_LEFT.alignment)
    }

    override fun setSize(width: Float, height: Float) {
        entityNameLabel.height = (if (x1Size) 25 else 43) * 2f
        val nameHeight = entityNameLabel.height
        super.setSize(width, height)
        textureButton.setSize(width, height - nameHeight)
        entityDrawer.setSize(width, height - nameHeight)

        textureButton.y = nameHeight
        entityDrawer.y = nameHeight
        entityNameLabel.width = width
        println("Width: $width, Height: $height, NameHeight: $nameHeight")
        println("${entityDrawer.width}, ${entityDrawer.height}")
    }
}
