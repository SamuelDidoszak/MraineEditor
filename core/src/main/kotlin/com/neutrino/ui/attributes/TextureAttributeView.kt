package com.neutrino.ui.attributes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
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
import com.neutrino.builders.TextureAttributeBuilder
import com.neutrino.builders.TextureBuilder
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.LightSources
import com.neutrino.textures.TextureSprite
import com.neutrino.textures.Textures
import com.neutrino.ui.attributes.util.PictureEditable
import com.neutrino.ui.elements.*
import com.neutrino.ui.views.AddRulesView
import com.neutrino.util.*

class TextureAttributeView(
    private val ATLAS_NAME: String = "entities.atlas"
): AttributeView(VisTable()) {

    override val attributeName = "Texture"
    private val textureTables = ArrayList<TextureTable>()
    private val texturesToAtlas = ArrayList<FileHandle>()
    private val characters = ATLAS_NAME.substringBefore('.') == "characters"

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
        val atlasSubstring = ATLAS_NAME.substringBefore('.')
        val textureAtlasGenerator = TextureAtlasGenerator(atlasSubstring)
        textureAtlasGenerator.generate(texturesToAtlas)
        val atlas = TextureAtlas(
            Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures/$ATLAS_NAME"))
        val textureBuilder = TextureBuilder()
        for (textureTable in textureTables) {
            if (textureTable.getAnimationState() == 0) {
                for (textureMap in textureTable.textures) {
                    textureBuilder.build(
                        textureMap.key,
                        atlasSubstring,
                        textureMap.value.lights,
                        textureMap.value.x,
                        textureMap.value.y,
                        textureMap.value.z
                    )
                    val newTextureSprite = TextureSprite(
                        atlas.findRegion(textureMap.key),
                        textureMap.value.x,
                        textureMap.value.y,
                        textureMap.value.z)
                    newTextureSprite.lights = textureMap.value.lights
                    Textures addOrReplace newTextureSprite
                }
            } else {
                textureBuilder.buildAnimation(
                    textureTable.textures.keys.toList(),
                    atlasSubstring,
                    textureTable.getAnimationState() == 1,
                    textureTable.getFps(textureTable.findActor("main")),
                    textureTable.animatedTextureSprite!!.lights,
                    textureTable.animatedTextureSprite!!.x,
                    textureTable.animatedTextureSprite!!.y,
                    textureTable.animatedTextureSprite!!.z,
                )
                val regionArray = Array<AtlasRegion>()
                textureTable.textures.keys.forEach { regionArray.add(atlas.findRegion(it)) }
                val newTextureSprite = AnimatedTextureSprite(
                    regionArray,
                    textureTable.getAnimationState() == 1,
                    textureTable.getFps(textureTable.findActor("main")),
                    textureTable.animatedTextureSprite!!.x,
                    textureTable.animatedTextureSprite!!.y,
                    textureTable.animatedTextureSprite!!.z,
                )
                newTextureSprite.lights = textureTable.animatedTextureSprite!!.lights
                Textures addOrReplace newTextureSprite
            }
        }
    }

    override fun generateString(): String {
        return TextureAttributeBuilder().generateString(textureTables)
    }

    override fun validateAttribute(): Boolean {
        if (textureTables.size == 1)
            return false
        return true
    }

    fun texturesExist(): Boolean {
        for (textureTable in textureTables) {
            val textureName = textureTable.textures.keys.firstOrNull()?.substringBefore('#')
            if (Textures.getOrNull(textureName) != null)
                return true
        }
        return false
    }

    inner class TextureTable(): VisTable() {
        val textures: MutableMap<String, TextureSprite> = object : LinkedHashMap<String, TextureSprite>() {
            override fun get(key: String): TextureSprite? {
                if (key == "main")
                    return values.first()
                return super.get(key)
            }
        }
        private var innerTables: Array<VisTable> = Array()
        private var tableAnimation: TableAnimation? = null
        var animatedTextureSprite: AnimatedTextureSprite? = null
        var rules: List<NameOrIdentity?>? = null
        var flipX: Boolean = false
        var flipY: Boolean = false
        var checkAll: Boolean = false

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

        private val textureLinkButton = object: TextureButton(Textures.get("chainTexture")) {
            var state = 0
                private set
            fun newState() {
                state += 1
                state %= 4
                changeTexture()
            }

            private fun changeTexture() {
                texture = when (state) {
                    0 -> Textures.get("chainTexture")
                    1 -> Textures.get("addTexture")
                    2 -> Textures.get("stopTexture")
                    3 -> Textures.get("chainStopTexture")
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
            mainTextureTable.add(addRulesTable(mainTextureTable)).growY().left()
            add(mainTextureTable).growX().row()

            val innerTextures = VisTable()
            innerTextures.name = "innerTextures"
            val collapsibleInnerTextures = CollapsibleWidget(innerTextures)
            collapsibleInnerTextures.name = "collapsibleTextures"
            collapsibleInnerTextures.isCollapsed = false
            add(collapsibleInnerTextures)

            val buttonsTable = VisTable()
            buttonsTable.name = "buttonsTable"
            val deleteButton = DeleteButton() {
                textureTables.remove(this)
                table.remove(this)
            }

            val expandButton = VisCheckBox("")
            expandButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    collapsibleInnerTextures.isCollapsed = !collapsibleInnerTextures.isCollapsed
                }
            })
            buttonsTable.add(deleteButton).top().right().row()
            buttonsTable.add(expandButton).bottom().expandY()
            mainTextureTable.add(buttonsTable).growY()

            setVisible(mainTextureTable, false)
        }

        private fun addInnerTable(name: String, textureSprite: TextureSprite): VisTable {
            val innerTable = VisTable()
            innerTable.name = name
            innerTable.add(addTextureContainer(innerTable, textureSprite)).size(128f).left().padLeft(60f)
            innerTable.add(addPositionTable(innerTable)).padLeft(8f)
            innerTable.add(addParametersTable(innerTable, false)).growY().left()

            val buttonsTable = VisTable()
            val deleteButton = DeleteButton {
                texturesToAtlas.removeIf { it.file().nameWithoutExtension == name }
                if (innerTables.size == 1) {
                    textureTables.remove(this)
                    table.remove(this)
                    return@DeleteButton
                }
                textures.remove(name)
                innerTables.removeValue(innerTable, true)
                findActor<CollapsibleWidget>("collapsibleTextures")
                    .findActor<VisTable>("innerTextures").remove(innerTable)
                setMainAnimationState(animationButton.state)
            }
            buttonsTable.add(deleteButton).expandY().top()
            innerTable.add(buttonsTable).growY()

            findActor<CollapsibleWidget>("collapsibleTextures")
                .findActor<VisTable>("innerTextures").add(innerTable).growX().padTop(8f).row()

            (getFromPositionTable("x", innerTable).model as IntSpinnerModel)
                .setValue(textureSprite.x.toInt(), false)
            (getFromPositionTable("y", innerTable).model as IntSpinnerModel)
                .setValue(textureSprite.y.toInt(), false)
            (getFromPositionTable("z", innerTable).model as IntSpinnerModel)
                .setValue(textureSprite.z, false)
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
                val textureView = TextureButtonWithBackground(textureSprite, characters)
                textureView.setSize(128f, 128f)
                textureView.setBackgroundColor()
                textureView.centered = false
                textureContainer.actor = textureView
            }
            return textureContainer
        }
        private fun addPositionTable(table: VisTable): VisTable {
            fun refreshMainTexture(newTexture: TextureSprite, position: String, value: Int) {
                if (table.name == "main") {
                    getTextureButton(innerTables.first()).texture = newTexture
                    (getFromPositionTable(position, innerTables.first()).model as IntSpinnerModel)
                        .setValue(value, false)
                } else {
                    val mainTable = findActor<VisTable>("main")
                    getTextureButton(mainTable).texture = newTexture
                    (getFromPositionTable(position, mainTable).model as IntSpinnerModel)
                        .setValue(value, false)
                }
            }
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
                    if (textures.size == 1)
                        refreshMainTexture(texture, "x", texture.x.toInt())
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
                    if (textures.size == 1)
                        refreshMainTexture(texture, "y", texture.y.toInt())
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
                    if (textures.size == 1)
                        refreshMainTexture(texture, "z", texture.z)
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
            lightsTable.width = 97f
            lightsTable.maxHeight = 43f
            if (table.name != "main")
                lightsTable.maxHeight = lightsTable.maxHeight!! * 2

            paramsTable.add(lightsTable).expandX().center()
            return paramsTable
        }
        private fun addRulesTable(table: VisTable): VisTable {
            val rulesTable = VisTable()
            rulesTable.name = "rulesTable"
            rulesTable.top()

            val rulePickerButton = RulePickerButton(Textures.get("rulesTexture"))
            rulePickerButton.setSize(100f, 100f)
            rulePickerButton.addListener(getChangeListener { _, actor ->
                stage.addActor(AddRulesView(rules?.toMutableList()) {
                    addRules(it.first, it.third.first, it.third.second, it.third.third, it.second, actor as RulePickerButton)
                })
            })

            val percentageTable = VisTable()
            percentageTable.height = 28f
            val probabilityText = TextField(getChainedProbability().toInt().toString())
            probabilityText.name = "probability"
            probabilityText.width = 55f // 75
            probabilityText.height = 28f
            textureLinkButton.setSize(36f, 36f)
            textureLinkButton.addListener(getChangeListener { _, _ ->
                textureLinkButton.newState()
                updateProbabilities()
            })
            percentageTable.add(textureLinkButton).padRight(4f)
            percentageTable.add(probabilityText).expandX()
            percentageTable.add(VisLabel("%"))

            rulesTable.add(rulePickerButton).row()
            rulesTable.add(percentageTable).expandX()

            return rulesTable
        }
        fun getProbability(): Float {
            try {
                return (children.get(0) as VisTable).findActor<TextField>("probability").text.toFloat()
            } catch (e: NumberFormatException) {
                return 0f
            }
        }
        fun setProbability(probability: Float) {
            (children.get(0) as VisTable).findActor<TextField>("probability")
                .text = probability.toInt().toString()
        }
        fun getChainedProbability(): Float {
            var probabilitySum = 100f
            if (textureTables.isEmpty())
                return probabilitySum

            var index = textureTables.size
            if (!children.isEmpty)
                index = textureTables.indexOf(this)
            if (index == -1)
                index = textureTables.size

            println("Calculating chained probability")
            for (i in (0 until index).reversed()) {
                if (!textureTables[i].isChained())
                    break
                probabilitySum -= textureTables[i].getProbability()
            }
            return probabilitySum
        }
        private var previousChainedProbability = 100f
        fun updateProbabilities() {
            var endIndex = textureTables.size
            if (!children.isEmpty)
                endIndex = textureTables.indexOf(this)
            if (endIndex == -1)
                endIndex = textureTables.size

            if (getTextureLinkState() == 3) {
                (children.get(0) as VisTable).findActor<TextField>("probability").text =
                    previousChainedProbability.toInt().toString()
                if (endIndex != 0 && textureTables[endIndex - 1].isChained())
                    textureTables[endIndex - 1].setProbability(
                        textureTables[endIndex - 1].getProbability() - previousChainedProbability)

            }
            if (getTextureLinkState() == 1) {
                previousChainedProbability = getProbability()
                (children.get(0) as VisTable).findActor<TextField>("probability").text = "100"
                if (endIndex != 0 && textureTables[endIndex - 1].isChained())
                    textureTables[endIndex - 1].setProbability(
                        textureTables[endIndex - 1].getProbability() + previousChainedProbability
                    )
            }
        }
        private fun addRules(
            ruleList: List<NameOrIdentity?>,
            flipX: Boolean,
            flipY: Boolean,
            checkAll: Boolean,
            newRulePickerButton: RulePickerButton,
            rulePickerButton: RulePickerButton) {
            var isEmpty = true
            for (rule in ruleList) {
                if (rule != null) {
                    isEmpty = false
                    break
                }
            }
            for (i in newRulePickerButton.textureList.indices) {
                rulePickerButton.setTexture(i, newRulePickerButton.textureList[i])
                rulePickerButton.setNot(i, ruleList[i]?.not == true)
            }
            if (isEmpty)
                rules = null
            else
                rules = ruleList

            this.flipX = flipX
            this.flipY = flipY
            this.checkAll = checkAll
        }

        override fun act(delta: Float) {
            tableAnimation?.nextFrame(delta)
            super.act(delta)
        }

        private fun setMainAnimationState(animationState: Int, textureView: TextureButtonWithBackground? = null) {
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
                val lightSources = LightSources()
                var hasLights = false
                textures.forEach {
                    regions.add(it.value.texture)
                    lightSources.add(it.value.lights?.getLights())
                    if ((it.value.lights?.isSingleLight == true && it.value.lights?.getLight() != null) ||
                        it.value.lights?.getLights() != null)
                        hasLights = true
                }
                animatedTextureSprite = if (hasLights)
                    AnimatedTextureSprite(
                        regions,
                        true,
                        getFps(table),
                        lightSources,
                        x, y, z
                    )
                else
                    AnimatedTextureSprite(
                        regions,
                        true,
                        getFps(table),
                        x, y, z
                    )

                val textureButton = textureView ?: getTextureButton(table)
                textureButton.texture = animatedTextureSprite!!

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

        // Helper classes
        private fun getTextureContainer(table: VisTable): Container<Actor> {
            return table.findActor("textureContainer")
        }
        private fun getTextureButton(table: VisTable): TextureButtonWithBackground {
            return (table.findActor("textureContainer") as Container<Actor>).actor as TextureButtonWithBackground
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
            table.findActor<VisTable>("rulesTable").isVisible = visible
            table.findActor<VisTable>("buttonsTable").isVisible = visible
        }
        fun getAnimationState(): Int {
            return animationButton.state
        }
        fun getTextureLinkState(): Int {
            return textureLinkButton.state
        }
        fun isChained(): Boolean {
            return textureLinkButton.state == 0 || textureLinkButton.state == 3
        }
        fun isBlock(): Boolean {
            return textureLinkButton.state == 2 || textureLinkButton.state == 3
        }

        fun hasRequirements(): Boolean {
            val requirements = rules?.filterNotNull()
            if (requirements.isNullOrEmpty())
                return false
            return true
        }

        private fun getFile(table: VisTable, textureView: TextureButtonWithBackground) {
            Constants.fileChooser.getFile(object: FileChooserCallback() {
                override fun fileChosen(files: List<FileHandle>) {
                    val x = getFromPositionTable("x", table).textField.text.toFloat()
                    val y = getFromPositionTable("y", table).textField.text.toFloat()
                    val z = getFromPositionTable("z", table).textField.text.toInt()

                    for (file in files) {
                        val newFile = FileHandle(file.file())
                        val picture = PictureEditable(newFile)
                        val xOffset = picture.getXOffset()
                        val yOffset = picture.getYOffset()
                        if (textures.keys.any { it.contains('#') } || files.any { it.name().contains('#') })
                            picture.editAnimation()
                        else
                            picture.edit()
                        val texture = picture.getEditedTexture()
                        val region = AtlasRegion(texture, 0, 0, texture.width, texture.height)
                        val textureSprite = TextureSprite(region, x + xOffset, y + yOffset, z)
                        textureSprite.lights = picture.getLightSources()

                        textures[newFile.nameWithoutExtension()] = textureSprite
                        texturesToAtlas.add(picture.getTempFile())
                        val innerTable = addInnerTable(newFile.nameWithoutExtension(), textureSprite)
                        innerTables.add(innerTable)

                        fillLightsTable(textureSprite, innerTable)
                    }

                    if (textures.size == 1) {
                        textureView.texture = textures.values.first()
                        (getFromPositionTable("x", table).model as IntSpinnerModel).setValue(textureView.texture.x.toInt(), false)
                        (getFromPositionTable("y", table).model as IntSpinnerModel).setValue(textureView.texture.y.toInt(), false)
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
                image.color = Color(it.color.r, it.color.g, it.color.b, 1f)
                lightsTable.addNested(image).size(32f).left()
            }
        }

        private inner class AddListener(val table: VisTable): ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val textureView = TextureButtonWithBackground(Textures.get("addButtonTexture"), characters)
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
