package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.Sizes
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.building.utilities.Alignment
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import com.kotcrab.vis.ui.widget.spinner.Spinner.SpinnerStyle
import com.neutrino.textures.*
import com.neutrino.ui.elements.TextureButton
import com.neutrino.util.Constants
import com.neutrino.util.FileChooserCallback
import com.neutrino.util.Pixel
import com.neutrino.util.PixelData

class TextureAttributeView: AttributeView(VisTable()) {

    override val attributeName = "TextureAttribute"
    private val addImage = TextureSprite(TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))
    private val textures = ArrayList<TextureParams>()
    private val texturesToAtlas = ArrayList<FileHandle>()

    init {
        TableUtils.setSpacingDefaults(table)
        addTextureView()
    }

    private fun addTextureView() {
        fun VisTable.addPosition(name: String, value: Int): Cell<Spinner> {
            add(VisLabel("$name: ", Alignment.LEFT.alignment))
            val sizes = Sizes(VisUI.getSizes())
            sizes.spinnerFieldSize = 56f
            sizes.spinnerButtonHeight -= 4f
            val txt = Spinner(VisUI.getSkin()["default", SpinnerStyle::class.java], sizes,"",
                IntSpinnerModel(value, -999, 999, 1))
            txt.name = name
            txt.maxLength = 3
            return add(txt).left()
        }

        class AddListener(val textureParams: TextureParams): ChangeListener() {
            fun getFile(textureView: TextureButton) {
                Constants.fileChooser.getFile(object: FileChooserCallback() {
                    override fun fileChosen(files: List<FileHandle>) {
                        for (file in files) {
                            val newFile = FileHandle(file.file())
                            textureParams.textures[newFile.nameWithoutExtension()] = Texture(newFile)
                            texturesToAtlas.add(file)
                        }

                        val x = textureParams.getFromPositionTable("x").textField.text.toInt()
                        val y = textureParams.getFromPositionTable("y").textField.text.toInt()

                        val sprite: TextureSprite =
                            if (textureParams.textures.size > 1) {
                                val regions = Array<TextureAtlas.AtlasRegion>()
                                textureParams.textures.forEach {
                                    regions.add(
                                        TextureAtlas.AtlasRegion(
                                            it.value, x, y, it.value.width, it.value.height
                                        )
                                    )
                                }
                                AnimatedTextureSprite(regions)
                            }
                            else {
                                val texture = textureParams.textures.values.first()
                                TextureSprite(
                                    TextureAtlas.AtlasRegion(texture, x, y, texture.width, texture.height)
                                )
                            }

                        textureView.texture = sprite
                    }
                })
            }

            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureView = TextureButton(addImage)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        getFile(textureView)
                        updateLights(textureParams,
                            Texture(Gdx.files.internal("standingTorch$2#1.png")))
                    }
                })
                getFile(textureView)
                textureParams.getTextureContainer().actor = textureView
                textureParams.setVisible()
                addTextureView()
            }
        }

        val textureTable = VisTable()
        val textureParams = TextureParams(textureTable)

        val textureContainer = Container<Actor>()
        textureContainer.name = "TextureContainer"
        textureTable.add(textureContainer).size(128f).left()
        val addButton = VisTextButton("+")
        textureContainer.actor = addButton
        addButton.pad(14f, 24f, 14f, 24f)
        textureContainer.setSize(128f, 128f)
        textureContainer.align(Align.center)
        addButton.addListener(AddListener(textureParams))

        val positionTable = VisTable()
        positionTable.name = "PositionTable"
        positionTable.isVisible = false
        positionTable.addPosition("x", 0).row()
        positionTable.addPosition("y", 0).row()
        positionTable.addPosition("z", 1)
        textureTable.add(positionTable).padLeft(8f)

        val paramsTable = VisTable()
        paramsTable.name = "ParametersTable"
        paramsTable.top()
        paramsTable.isVisible = false

        val animationButton = object : TextureButton(Textures.get("animationButton")) {
            var state = 0
                private set
            fun newState() {
                state += 1
                state %= 3
                changeTexture()
            }

            private fun changeTexture() {
                texture = when (state) {
                    0 -> Textures.get("animationButton")
                    1 -> Textures.get("animationButtonLooping")
                    2 -> Textures.get("animationButtonForward")
                    else -> {texture}
                }
            }
        }
        animationButton.setSize(42f, 42f)
        val sizes = Sizes(VisUI.getSizes())
        sizes.spinnerFieldSize = 40f
        sizes.spinnerButtonHeight -= 4f
        val fps = Spinner(VisUI.getSkin()["default", SpinnerStyle::class.java], sizes,"",
            IntSpinnerModel(4, 1, 99, 1))
        fps.name = "fps"
        fps.maxLength = 2
        fps.isVisible = false
        animationButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                animationButton.newState()
                fps.isVisible = animationButton.state != 0
            }
        })

        val animatedTable = VisTable()
        animatedTable.name = "animated"
        animatedTable.top()
        animatedTable.add(animationButton).left()
        animatedTable.add(fps).left()

        val lightsTable = VisTable()
        lightsTable.name = "LightsTable"
        lightsTable.top()
        lightsTable.add(VisLabel("Lights")).top().center().row()

        paramsTable.add(animatedTable).left().row()
        paramsTable.add(lightsTable).center()
        textureTable.add(paramsTable).growY().left()

        textures.add(textureParams)
        table.add(textureParams.table).growX().row()
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

    private class TextureParams(val table: VisTable) {
        val textures = mutableMapOf<String, Texture>()
        var lights: LightSources? = null

        fun getLightsTable(): VisTable {
            return table.findActor<VisTable>("LightsTable")
        }

        fun getTextureContainer(): Container<Actor> {
            return table.findActor("TextureContainer")
        }

        fun getFromPositionTable(positionName: String): Spinner {
            return table.findActor<VisTable>("PositionTable").findActor(positionName)!!
        }

        fun setVisible() {
            table.findActor<VisTable>("PositionTable").isVisible = true
            table.findActor<VisTable>("ParametersTable").isVisible = true
        }
    }
}








