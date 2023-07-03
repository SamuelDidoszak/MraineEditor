package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
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
import com.neutrino.builders.TextureBuilder
import com.neutrino.textures.*
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.util.Constants
import com.neutrino.util.FileChooserCallback
import com.neutrino.util.Pixel
import com.neutrino.util.PixelData

class TextureAttributeView: AttributeView(VisTable()) {

    override val attributeName = "TextureAttribute"
    private val ATLAS_NAME = "entities.atlas"
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

    override fun onSaveAction() {
        val textureBuilder = TextureBuilder()
        //TODO add textures to atlas
        for (textureTable in textureTables) {
            if (textureTable.getAnimationState() == 0) {
                for (textureMap in textureTable.textures) {
                    textureBuilder.build(
                        textureMap.key,
                        ATLAS_NAME,
                        textureMap.value.x,
                        textureMap.value.y,
                        textureMap.value.z
                    )
                }
            } else {
                textureBuilder.buildAnimation(
                    textureTable.textures.keys.toList(),
                    ATLAS_NAME,
                    textureTable.getAnimationState() == 1,
                    textureTable.getFps(textureTable.findActor("main")),
                    textureTable.animatedTextureSprite!!.x,
                    textureTable.animatedTextureSprite!!.y,
                    textureTable.animatedTextureSprite!!.z,
                )
            }
        }
    }

    override fun generateString(): String {
        val builder = StringBuilder(300)
        fun addIndicedLine(indices: Int = 0) {
            builder.append("\n\t\t")
            for (i in 0 until indices)
                builder.append("\t")
        }
        fun addTexture(textureName: String) {
            builder.append("textures.add(Textures.get(\"$textureName\")")
        }

        builder.append("TextureAttribute { position, random, textures -> run {")
        for (textureTable in textureTables) {
            addIndicedLine(1)
            if (textureTable.getAnimationState() == 0) {
                //TODO random texture with probability from provided set
                addTexture(textureTable.textures.keys.first())
            } else {
                addTexture(textureTable.textures.keys.first().substringBefore('#'))
            }
        }

        addIndicedLine()
        builder.append("}})")
        return builder.toString()
    }

    private inner class TextureTable(): VisTable() {
        val textures: MutableMap<String, TextureSprite> = object : LinkedHashMap<String, TextureSprite>() {
            override fun get(key: String): TextureSprite? {
                if (key == "main")
                    return values.first()
                return super.get(key)
            }
        }
        private var innerTables: Array<VisTable>? = null
        private var tableAnimation: TableAnimation? = null
        var animatedTextureSprite: AnimatedTextureSprite? = null

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
                    val texture =
                        if (getTextureButton(table).texture is AnimatedTextureSprite)
                            animatedTextureSprite
                        else
                            textures[table.name]
                    texture!!.x = (actor as Group?)?.children?.get(1).toString().toFloat()
                    getTextureButton(table).texture = texture
                }
            })
            positionTable.row()
            val ySpinner = positionTable.addPosition("y", 0).actor
            ySpinner.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    val texture =
                        if (getTextureButton(table).texture is AnimatedTextureSprite)
                            animatedTextureSprite
                        else
                            textures[table.name]
                    texture!!.y = (actor as Group?)?.children?.get(1).toString().toFloat()
                    getTextureButton(table).texture = texture
                }
            })
            positionTable.row()
            val zSpinner = positionTable.addPosition("z", 1).actor
            zSpinner.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    val texture =
                        if (getTextureButton(table).texture is AnimatedTextureSprite)
                            animatedTextureSprite
                        else
                            textures[table.name]
                    texture!!.z = (actor as Group?)?.children?.get(1).toString().toInt()
                    getTextureButton(table).texture = texture
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

                        setMainAnimationState(animationButton.state)
                    }
                })
                animationButton.addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        animationButton.newState()
                        fps.isVisible = animationButton.state != 0
                        setMainAnimationState(animationButton.state)
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
            lightsTable.maxHeight = 43f
            if (table.name != "main")
                lightsTable.maxHeight = lightsTable.maxHeight!! * 2

            paramsTable.add(lightsTable).expandX().left()
            return paramsTable
        }

        override fun act(delta: Float) {
            tableAnimation?.nextFrame(delta)
            super.act(delta)
        }

        private fun setMainAnimationState(animationState: Int, textureView: TextureButton? = null) {
            tableAnimation?.restoreMainTable()
            if (animationState == 0) {
                tableAnimation = TableAnimation()
            } else {
                val table = findActor<VisTable>("main")

                if (animatedTextureSprite != null) {
                    getFromPositionTable("x", table).textField.text = animatedTextureSprite!!.x.toInt().toString()
                    getFromPositionTable("y", table).textField.text = animatedTextureSprite!!.y.toInt().toString()
                    getFromPositionTable("z", table).textField.text = animatedTextureSprite!!.z.toString()
                }

                val x = getFromPositionTable("x", table).textField.text.toFloat()
                val y = getFromPositionTable("y", table).textField.text.toFloat()
                val z = getFromPositionTable("z", table).textField.text.toInt()
                val regions = Array<AtlasRegion>()
                textures.forEach {
                    regions.add(it.value.texture)
                }
                animatedTextureSprite = AnimatedTextureSprite(
                    regions,
                    true,
                    getFps(table),
                    x, y, z
                )
                if (textureView != null)
                    textureView.texture = animatedTextureSprite!!
                else
                    getTextureButton(table).texture = animatedTextureSprite!!
                tableAnimation = TableAnimation(getFps(table), false, false, false)
            }
        }

        private inner class TableAnimation(
            frameDuration: Float = 1f,
            disablePosition: Boolean = true,
            val changeTexture: Boolean = true,
            val changePosition: Boolean = true) {

            private val mainTable = findActor<VisTable>("main")
            private val mainX = getFromPositionTable("x", mainTable).textField.text
            private val mainY = getFromPositionTable("y", mainTable).textField.text
            private val mainZ = getFromPositionTable("z", mainTable).textField.text
            private var stateTime = 0f
            private val tableAnimation: Animation<VisTable> = Animation(frameDuration, innerTables, Animation.PlayMode.LOOP)

            init {
                if (disablePosition) {
                    getFromPositionTable("x", mainTable).isDisabled = true
                    getFromPositionTable("y", mainTable).isDisabled = true
                    getFromPositionTable("z", mainTable).isDisabled = true
                }
            }

            fun setFrameDuration(frameDuration: Float) {
                tableAnimation.frameDuration = frameDuration
            }

            fun nextFrame(deltaTime: Float) {
                stateTime += deltaTime
                val nextTable = tableAnimation.getKeyFrame(stateTime)
                fillLightsTable(getTextureButton(nextTable).texture, mainTable)
                if (changeTexture)
                    getTextureButton(mainTable).texture = getTextureButton(nextTable).texture
                if (!changePosition)
                    return
                getFromPositionTable("x", mainTable).textField.text = getFromPositionTable("x", nextTable).textField.text
                getFromPositionTable("y", mainTable).textField.text = getFromPositionTable("y", nextTable).textField.text
                getFromPositionTable("z", mainTable).textField.text = getFromPositionTable("z", nextTable).textField.text
            }

            fun restoreMainTable() {
                getFromPositionTable("x", mainTable).isDisabled = false
                getFromPositionTable("y", mainTable).isDisabled = false
                getFromPositionTable("z", mainTable).isDisabled = false
                getFromPositionTable("x", mainTable).textField.text = mainX
                getFromPositionTable("y", mainTable).textField.text = mainY
                getFromPositionTable("z", mainTable).textField.text = mainZ
            }
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
        private fun getTextureContainer(table: VisTable): Container<Actor> {
            return table.findActor("textureContainer")
        }
        private fun getTextureButton(table: VisTable): TextureButton {
            return (table.findActor("textureContainer") as Container<Actor>).actor as TextureButton
        }
        fun getFromPositionTable(positionName: String, table: VisTable): Spinner {
            return table.findActor<VisTable>("positionTable").findActor(positionName)!!
        }
        private fun getFpsSpinner(table: VisTable): Spinner {
            return table.findActor<VisTable>("parametersTable")
                .findActor<VisTable>("animatedTable").findActor("fps")
        }
        fun getFps(table: VisTable): Float {
            val fps = getFpsSpinner(table).textField.text.toInt()
            return 1f / fps
        }
        private fun setVisible(table: VisTable, visible: Boolean) {
            table.findActor<VisTable>("positionTable").isVisible = visible
            table.findActor<VisTable>("parametersTable").isVisible = visible
        }
        fun getAnimationState(): Int {
            return animationButton.state
        }

        private fun getFile(table: VisTable, textureView: TextureButton) {
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
                        if (innerTables == null)
                            innerTables = Array()
                        innerTables!!.add(innerTable)

                        fillLightsTable(textureSprite, innerTable)
                    }

                    if (textures.size == 1) {
                        textureView.texture = textures.values.first()
                        fillLightsTable(textureView.texture, table)
                        return
                    }

                    if (textures.keys.any { it.contains('#') }) {
                        animationButton.setAnimated(true)
                        getFpsSpinner(table).isVisible = true
                    }
                    else
                        animationButton.setAnimated(false)

                    setMainAnimationState(animationButton.state, textureView)
                }
            })
        }

        private fun fillLightsTable(textureSprite: TextureSprite, table: VisTable) {
            val lightsTable = table.findActor<VisTable>("parametersTable")
                .findActor<VisTableNested>("lightsTable")
            lightsTable.removeAll()
            lightsTable.left()

            textureSprite.lights?.getLights()?.forEach {
                val image = Image(VisUI.getSkin().getDrawable("white"))
                image.setSize(32f, 32f)
                image.color = it.color
                lightsTable.addNested(image).size(32f).left()
            }
        }

        private inner class AddListener(val table: VisTable): ChangeListener() {
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








