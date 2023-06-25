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
import com.kotcrab.vis.ui.widget.*
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import com.kotcrab.vis.ui.widget.spinner.Spinner.SpinnerStyle
import com.neutrino.textures.*
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.util.Constants
import com.neutrino.util.FileChooserCallback
import com.neutrino.util.Pixel
import com.neutrino.util.PixelData

class TextureAttributeView: AttributeView(VisTable()) {

    override val attributeName = "TextureAttribute"
    private val addImage = TextureSprite(TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))
    private val textureTables = ArrayList<TextureTable>()
    private val texturesToAtlas = ArrayList<FileHandle>()

    init {
        TableUtils.setSpacingDefaults(table)
        addTextureTable()
    }

    fun addTextureTable() {
        val textureTable = TextureTable()
        textureTables.add(textureTable)
        table.add(textureTable).growX().row()
    }

    private inner class TextureTable(): VisTable() {
        val textures: MutableMap<String, TextureSprite> = object : LinkedHashMap<String, TextureSprite>() {
            override fun get(key: String): TextureSprite? {
                if (key == "main")
                    return values.first()
                return super.get(key)
            }
        }
        private var innerTables: ArrayList<VisTable>? = null

        private val animationButton = object: TextureButton(Textures.get("animationButton")) {
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

        init {
            val mainTextureTable = VisTable()
            mainTextureTable.name = "main"
            mainTextureTable.add(addTextureContainer(mainTextureTable)).size(128f).left()
            mainTextureTable.add(addPositionTable(mainTextureTable)).padLeft(8f)
            mainTextureTable.add(addParametersTable(mainTextureTable)).growY().left()
            setVisible(mainTextureTable, false)
            add(mainTextureTable).growX().row()

            val innerTextures = VisTable()
            innerTextures.name = "innerTextures"
            val collapsibleInnerTextures = CollapsibleWidget(innerTextures)
            collapsibleInnerTextures.name = "collapsibleTextures"
            collapsibleInnerTextures.isCollapsed = false
            val expandButton = VisCheckBox("")
            expandButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    collapsibleInnerTextures.isCollapsed = !collapsibleInnerTextures.isCollapsed
                }
            })
            add(collapsibleInnerTextures)
            mainTextureTable.add(expandButton).grow().right().bottom()
        }

        private fun addInnerTable(name: String, textureSprite: TextureSprite): VisTable {
            val innerTable = VisTable()
            innerTable.name = name
            innerTable.add(addTextureContainer(innerTable, textureSprite)).size(128f).left().padLeft(60f)
            innerTable.add(addPositionTable(innerTable)).padLeft(8f)
            innerTable.add(addParametersTable(innerTable, false)).growY().left()
            findActor<CollapsibleWidget>("collapsibleTextures")
                .findActor<VisTable>("innerTextures").add(innerTable).growX().padTop(8f).row()
            return innerTable
        }

        private fun VisTable.addPosition(name: String, value: Int): Cell<Spinner> {
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
        private fun addTextureContainer(table: VisTable, textureSprite: TextureSprite? = null): Container<Actor> {
            val textureContainer = Container<Actor>()
            textureContainer.name = "textureContainer"
            if (textureSprite == null) {
                val addButton = VisTextButton("+")
                addButton.pad(14f, 24f, 14f, 24f)
                textureContainer.setSize(128f, 128f)
                textureContainer.align(Align.center)
                addButton.addListener(AddListener(table))
                textureContainer.actor = addButton
            } else {
                val textureView = TextureButton(textureSprite)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.centered = false
                textureView.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        getFile(table, textureView)
                    }
                })
                textureContainer.actor = textureView
            }
            return textureContainer
        }
        private fun addPositionTable(table: VisTable): VisTable {
            val positionTable = VisTable()
            positionTable.name = "positionTable"
            val xSpinner = positionTable.addPosition("x", 0).actor
            xSpinner.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    textures[table.name]!!.x = (actor as Group?)?.children?.get(1).toString().toFloat()
                    getTextureButton(table).texture = textures[table.name]!!
                }
            })
            positionTable.row()
            val ySpinner = positionTable.addPosition("y", 0).actor
            ySpinner.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    textures[table.name]!!.y = (actor as Group?)?.children?.get(1).toString().toFloat()
                    getTextureButton(table).texture = textures[table.name]!!
                }
            })
            positionTable.row()
            val zSpinner = positionTable.addPosition("z", 1).actor
            zSpinner.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    textures[table.name]!!.z = (actor as Group?)?.children?.get(1).toString().toInt()
                    getTextureButton(table).texture = textures[table.name]!!
                }
            })
            return positionTable
        }
        private fun addParametersTable(table: VisTable, addAnimationButton: Boolean = true): VisTable {
            fun getAnimationTable(): VisTable {
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
                        val textureButton = getTextureButton(table)
                        if (textureButton.texture !is AnimatedTextureSprite || animationButton.state == 0)
                            return

                        val x = getFromPositionTable("x", table).textField.text.toFloat()
                        val y = getFromPositionTable("y", table).textField.text.toFloat()
                        val z = getFromPositionTable("z", table).textField.text.toInt()
                        val regions = Array<AtlasRegion>()
                        textures.forEach {
                            regions.add(it.value.texture)
                        }
                        textureButton.texture = AnimatedTextureSprite(
                            regions,
                            animationButton.state != 2,
                            if (animationButton.state == 0) 1f else getFps(table),
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
                return animatedTable
            }

            val paramsTable = VisTable()
            paramsTable.name = "parametersTable"
            paramsTable.top()

            if (addAnimationButton)
                paramsTable.add(getAnimationTable()).left().row()
            paramsTable.add(VisLabel("Lights")).top().center().row()

            val lightsTable = VisTableNested()
            lightsTable.name = "lightsTable"
            lightsTable.top()
            lightsTable.width = 112f

            paramsTable.add(lightsTable).expandX().left()
            return paramsTable
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

        // Helper classes
        fun getTextureContainer(table: VisTable): Container<Actor> {
            return table.findActor("textureContainer")
        }
        fun getTextureButton(table: VisTable): TextureButton {
            return (table.findActor("textureContainer") as Container<Actor>).actor as TextureButton
        }
        fun getFromPositionTable(positionName: String, table: VisTable): Spinner {
            return table.findActor<VisTable>("positionTable").findActor(positionName)!!
        }
        fun getFpsSpinner(table: VisTable): Spinner {
            return table.findActor<VisTable>("parametersTable")
                .findActor<VisTable>("animatedTable").findActor("fps")
        }
        fun getFps(table: VisTable): Float {
            val fps = getFpsSpinner(table).textField.text.toInt()
            return 1f / fps
        }
        fun setVisible(table: VisTable, visible: Boolean) {
            table.findActor<VisTable>("positionTable").isVisible = visible
            table.findActor<VisTable>("parametersTable").isVisible = visible
        }

        fun getFile(table: VisTable, textureView: TextureButton) {
            Constants.fileChooser.getFile(object: FileChooserCallback() {
                override fun fileChosen(files: List<FileHandle>) {
                    val x = getFromPositionTable("x", table).textField.text.toFloat()
                    val y = getFromPositionTable("y", table).textField.text.toFloat()
                    val z = getFromPositionTable("z", table).textField.text.toInt()

                    for (file in files) {
                        val newFile = FileHandle(file.file())
                        val texture = Texture(newFile)
                        val region = AtlasRegion(texture, 0, 0, texture.width, texture.height)
                        val textureSprite = TextureSprite(region, x, y, z)
                        textureSprite.lights = getLightSources(texture)

                        textures[newFile.nameWithoutExtension()] = textureSprite
                        texturesToAtlas.add(file)
                        val innerTable = addInnerTable(newFile.nameWithoutExtension(), textureSprite)

                        textureSprite.lights?.getLights()?.forEach {
                            val image = Image(VisUI.getSkin().getDrawable("white"))
                            image.setSize(32f, 32f)
                            image.color = it.color
                            innerTable.findActor<VisTable>("parametersTable")
                                .findActor<VisTableNested>("lightsTable").addNested(image).size(32f).left()
                        }
                    }

                    if (textures.size == 1) {
                        textureView.texture = textures.values.first()
                        return
                    }

                    if (textures.keys.any { it.contains('#') }) {
                        animationButton.setAnimated(true)
                        getFpsSpinner(table).isVisible = true
                    }
                    else
                        animationButton.setAnimated(false)

                    val regions = Array<AtlasRegion>()
                    textures.forEach {
                        regions.add(it.value.texture)
                    }
                    textureView.texture = AnimatedTextureSprite(
                        regions,
                        animationButton.state != 2,
                        if (animationButton.state == 0) 1f else getFps(table),
                        x, y, z
                    )
                }
            })
        }

        inner class AddListener(val table: VisTable): ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureView = TextureButton(addImage)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.centered = false
                textureView.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        getFile(table, textureView)
                    }
                })
                getFile(table, textureView)
                getTextureContainer(table).actor = textureView
                setVisible(table, true)
                addTextureTable()
            }
        }
    }
}








