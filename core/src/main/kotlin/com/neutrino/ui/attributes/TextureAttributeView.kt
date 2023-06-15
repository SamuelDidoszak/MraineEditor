package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.building.utilities.Alignment
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.Light
import com.neutrino.textures.LightSources
import com.neutrino.textures.TextureSprite
import com.neutrino.ui.elements.TextureButton
import com.neutrino.util.Pixel
import com.neutrino.util.PixelData
import com.neutrino.util.set

class TextureAttributeView: AttributeView(VisTable()) {

    override val attributeName = "TextureAttribute"
    private val addImage = TextureSprite(TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))
    private val textures = ArrayList<TextureParams>()

    init {
        TableUtils.setSpacingDefaults(table)
        addTextureView()
    }

    private fun addTextureView() {
        fun VisTable.addPosition(name: String, value: String): Cell<VisTextField> {
            add(VisLabel("$name: ", Alignment.LEFT.alignment))
            val txt = VisTextField(value)
            txt.setAlignment(Alignment.LEFT.alignment)
            txt.maxLength = 3
            return add(txt).left().maxWidth(64f)
        }
        class AddListener(val textureParams: TextureParams): ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureView = TextureButton(addImage)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        textureView.texture = animatedSprite
                        updateLights(textureParams,
                            Texture(Gdx.files.internal("standingTorch$2#1.png")))
                    }
                })

                textureParams.getTextureContainer().actor = textureView
                textureParams.setVisible()
                addTextureView()
            }
        }

        val textureTable = VisTable()
        val textureParams = TextureParams(textureTable)

        val textureContainer = Container<Actor>()
        textureContainer.name = "TextureContainer"
        textureTable.add(textureContainer).left().padRight(40f)
        val addButton = VisTextButton("+")
        textureContainer.actor = addButton
        addButton.setSize(128f, 128f)
        textureContainer.setSize(128f, 128f)
        textureContainer.align(Align.center)
        addButton.addListener(AddListener(textureParams))

        val positionTable = VisTable()
        positionTable.name = "PositionTable"
        positionTable.isVisible = false
        positionTable.addPosition("x", "0").row()
        positionTable.addPosition("y", "0").row()
        positionTable.addPosition("z", "1")
        textureTable.add(positionTable).width(64f).padLeft(40f)

        val lightsTable = VisTable()
        lightsTable.name = "LightsTable"
        lightsTable.isVisible = false
        lightsTable.top()
        lightsTable.add(VisLabel("Lights")).top().row()
        textureTable.add(lightsTable).growY().left()

        textures.add(textureParams)
        table.add(textureParams.table).growX().row()
        textureContainer.setSize(128f, 128f)
    }

    private fun updateLights(params: TextureParams, texture: Texture) {
        val pixelData = PixelData(texture)
        var pixel: Pixel
        val lights: ArrayList<Light> = ArrayList()

        for (y in 0 until texture.height) {
            for (x in 0 until texture.width) {
                pixel = pixelData.getPixel(x, y)
                if (pixel.a() in 100..250)
                    lights.add(Light(x.toFloat(), y.toFloat(), pixel.color()))
            }
        }

        if (lights.isNotEmpty()) {
            if (params.lights == null)
                params.lights = LightSources(lights)
            else
                params.lights!!.add(lights)
        }

        lights.forEach {
            val image = Image(VisUI.getSkin().getDrawable("white"))
            image.color = it.color
            params.getLightsTable().add(image).size(32f).left()
        }
        params.getLightsTable().row()
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

    private class TextureParams(val table: VisTable) {
        var lights: LightSources? = null

        fun getLightsTable(): VisTable {
            return table.findActor<VisTable>("LightsTable")
        }

        fun getTextureContainer(): Container<Actor> {
            return table.findActor("TextureContainer")
        }

        fun setVisible() {
            table.findActor<VisTable>("PositionTable").isVisible = true
            table.findActor<VisTable>("LightsTable").isVisible = true
        }
    }
}








