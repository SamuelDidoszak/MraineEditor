package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
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

        val animationButton = object: TextureButton(Textures.get("animationButton")) {
            var state = 0
                private set
            fun newState() {
                state += 1
                state %= 3
                changeTexture()
            }

            fun setAnimated(animated: Boolean) {
                state = if (animated) 1 else 0
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

        class AddListener(val textureParams: TextureParams): ChangeListener() {
            fun getFile(textureView: TextureButton) {
                Constants.fileChooser.getFile(object: FileChooserCallback() {
                    override fun fileChosen(files: List<FileHandle>) {
                        val x = textureParams.getFromPositionTable("x").textField.text.toFloat()
                        val y = textureParams.getFromPositionTable("y").textField.text.toFloat()
                        val z = textureParams.getFromPositionTable("z").textField.text.toInt()

                        for (file in files) {
                            val newFile = FileHandle(file.file())
                            val texture = Texture(newFile)
                            val region = AtlasRegion(texture, 0, 0, texture.width, texture.height)
                            val textureSprite = TextureSprite(region, x, y, z)
                            textureSprite.lights = getLightSources(texture)

                            textureParams.textureSprites[newFile.nameWithoutExtension()] = textureSprite
                            texturesToAtlas.add(file)
                        }

                        if (textureParams.textureSprites.size == 1) {
                            textureView.texture = textureParams.textureSprites.values.first()
                            return
                        }

                        if (textureParams.textureSprites.keys.any { it.contains('#') }) {
                            animationButton.setAnimated(true)
                            textureParams.getFpsSpinner().isVisible = true
                        }
                        else
                            animationButton.setAnimated(false)

                        val regions = Array<AtlasRegion>()
                        textureParams.textureSprites.forEach {
                            regions.add(it.value.texture)
                        }
                        textureView.texture = AnimatedTextureSprite(
                            regions,
                            animationButton.state != 2,
                            if (animationButton.state == 0) 1f else textureParams.getFps(),
                            x, y, z
                        )
                    }
                })
            }

            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureView = TextureButton(addImage)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.centered = false
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
        positionTable.name = "positionTable"
        positionTable.isVisible = false
        val xSpinner = positionTable.addPosition("x", 0).actor
        xSpinner.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureButton = (textureContainer.actor as TextureButton)
                textureButton.texture.x = (actor as Group?)?.children?.get(1).toString().toFloat()
                textureButton.texture = textureButton.texture
            }
        })
        positionTable.row()
        val ySpinner = positionTable.addPosition("y", 0).actor
        ySpinner.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureButton = (textureContainer.actor as TextureButton)
                textureButton.texture.y = (actor as Group?)?.children?.get(1).toString().toFloat()
                textureButton.texture = textureButton.texture
            }
        })
        positionTable.row()
        positionTable.addPosition("z", 1)
        textureTable.add(positionTable).padLeft(8f)

        val paramsTable = VisTable()
        paramsTable.name = "parametersTable"
        paramsTable.top()
        paramsTable.isVisible = false

        animationButton.setSize(42f, 42f)
        val sizes = Sizes(VisUI.getSizes())
        sizes.spinnerFieldSize = 40f
        sizes.spinnerButtonHeight -= 4f
        val fps = Spinner(VisUI.getSkin()["default", SpinnerStyle::class.java], sizes,"",
            IntSpinnerModel(4, 1, 99, 1))
        fps.name = "fps"
        fps.maxLength = 2
        fps.isVisible = false
        fps.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureButton = (textureContainer.actor as TextureButton)
                if (textureButton.texture !is AnimatedTextureSprite || animationButton.state == 0)
                    return

                val x = textureParams.getFromPositionTable("x").textField.text.toFloat()
                val y = textureParams.getFromPositionTable("y").textField.text.toFloat()
                val z = textureParams.getFromPositionTable("z").textField.text.toInt()
                val regions = Array<AtlasRegion>()
                textureParams.textureSprites.forEach {
                    regions.add(it.value.texture)
                }
                textureButton.texture = AnimatedTextureSprite(
                    regions,
                    animationButton.state != 2,
                    if (animationButton.state == 0) 1f else textureParams.getFps(),
                    x, y, z
                )
            }
        })
        animationButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                animationButton.newState()
                fps.isVisible = animationButton.state != 0
            }
        })

        val animatedTable = VisTable()
        animatedTable.name = "animatedTable"
        animatedTable.top()
        animatedTable.add(animationButton).left()
        animatedTable.add(fps).left()

        val lightsTable = VisTable()
        lightsTable.name = "lightsTable"
        lightsTable.top()
        lightsTable.add(VisLabel("Lights")).top().center().row()

        paramsTable.add(animatedTable).left().row()
        paramsTable.add(lightsTable).center()
        textureTable.add(paramsTable).growY().left()

        textures.add(textureParams)
        table.add(textureParams.table).growX().row()
    }

    private fun getLightSources(texture: Texture): LightSources? {
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

        if (lights.isEmpty())
            return null
        if (lights.size == 1)
            return LightSources(lights[0])
        else
            return LightSources(lights)
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
        val textureSprites = mutableMapOf<String, TextureSprite>()
        var lights: LightSources? = null

        fun getLightsTable(): VisTable {
            return table.findActor<VisTable>("LightsTable")
        }

        fun getTextureContainer(): Container<Actor> {
            return table.findActor("TextureContainer")
        }

        fun getFromPositionTable(positionName: String): Spinner {
            return table.findActor<VisTable>("positionTable").findActor(positionName)!!
        }

        fun getFpsSpinner(): Spinner {
            return table.findActor<VisTable>("parametersTable")
                .findActor<VisTable>("animatedTable").findActor("fps")
        }

        fun getFps(): Float {
            val fps = getFpsSpinner().textField.text.toInt()
            return 1f / fps
        }

        fun setVisible() {
            table.findActor<VisTable>("positionTable").isVisible = true
            table.findActor<VisTable>("parametersTable").isVisible = true
        }
    }
}








