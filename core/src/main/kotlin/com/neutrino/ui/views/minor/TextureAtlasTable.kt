package com.neutrino.ui.views.minor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.util.dialog.OptionDialogAdapter
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.builders.TextureBuilder
import com.neutrino.entities.Entity
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.TextureSprite
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.views.AddNewTextureView
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.ui.views.util.Search
import com.neutrino.ui.views.util.UiTab
import com.neutrino.util.TextureAtlasGenerator
import com.neutrino.util.UiManagerFactory
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.popupMenu

class TextureAtlasTable(
    val atlasName: String,
    val search: Search<EntityButton>? = null,
): VisTable() {
    private val folder = Gdx.files.absolute("${Gdx.files.localStoragePath}/textureSources/$atlasName/")
    private var atlas = TextureAtlas(Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures/$atlasName.atlas"))

    private val textureList = ArrayList<TextureSprite>()
    private val textureButtonList = ArrayList<EntityButton>()

    private val nameX1Size = true
    private val nameHeight = (if (nameX1Size) 25 else 43) * 2
    private val BUTTON_WIDTH = 128f
    private val BUTTON_HEIGHT = BUTTON_WIDTH + nameHeight

    init {
        this.setFillParent(false)
        clip(true)
        fillTextureList()
        initializeTable()
        initializeTextures()
        fillTextureTable()
    }

    private fun fillTextureList() {
        for (texture in Textures.getAllTextures()) {
            if (atlas.findRegion(texture.texture.name) == null)
                continue
            textureList.add(texture)
        }
    }

    private fun initializeTable(containerCount: Int = folder.list().size) {
        fun addAddButtonContainer() {
            val container = Container<Actor>()
            val addButton = TextureButton(Textures.get("addButtonTexture"))
            addButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    addNewTexture()
                }
            })
            container.actor = addButton
            container.actor.setSize(BUTTON_WIDTH, BUTTON_WIDTH)
            container.top()
            add(container).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
        }

        var rows = (containerCount + 1) / 4 + if ((containerCount + 1) % 4 != 0) 1 else 0

        inner@ for (n in 0 until rows) {
            for (i in 0 until 4) {
                val cellNumber = n * 4 + i
                if (cellNumber == 0) {
                    addAddButtonContainer()
                    continue
                }
                val container = Container<Actor>()
                container.name = (cellNumber).toString()
                add(container).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
            }
            row()
        }

        top()
        left()
        pack()
        layout()
    }

    private fun addNewTexture() {
        UiManagerFactory.getUI().setLeftPanel(AddNewTextureView(atlasName) {
            if (it) {
                atlas = TextureAtlas(Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures/$atlasName.atlas"))
                refreshTable()
            }
        }, UiTab.TexturesTab)
    }

    private fun initializeTextures() {
        for (i in textureList.indices) {
            addTextureButton(textureList[i])
        }
    }

    private fun fillTextureTable(textureButtons: List<EntityButton>? = null) {
        val entities = textureButtons ?: textureButtonList
        for (i in entities.indices) {
            val container = (children[i + 1] as Container<*>)
            val textureButton = entities[i]
            container.actor = textureButton
        }
    }

    private fun addTextureButton(textureSprite: TextureSprite, index: Int? = null): EntityButton {
        val entity = Entity()
        entity.name = textureSprite.texture.name.substringBefore('#')
        entity.addAttribute(com.neutrino.entities.attributes.Texture { _, _, textures ->
            textures.add(textureSprite)
        })

        val entityButton = EntityButton(entity, true, nameX1Size, false)
        entityButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        entityButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val menu = scene2d.popupMenu {
                    addItem(menuItem("delete") {
                        onClick { deleteTexturePopup(entity.name) }
                    })
                }
                menu.showMenu(stage, entityButton)
            }
        })
        if (index != null)
            textureButtonList[index] = entityButton
        else
            textureButtonList.add(entityButton)
        search?.data?.add(Pair(entityButton, entity.name))
        return entityButton
    }

    fun displaySearchResults(searchResults: List<EntityButton>) {
        clearChildren()
        initializeTable(searchResults.size)
        fillTextureTable(searchResults)
    }

    private fun refreshTable() {
        clearChildren()
        textureList.clear()
        textureButtonList.clear()
        search?.data?.clear()
        fillTextureList()
        initializeTable()
        initializeTextures()
        fillTextureTable()
        search?.refresh()
    }

    private fun deleteTexturePopup(name: String) {
        Dialogs.showOptionDialog(stage,
            "Delete texture",
            "do you want to delete \n$name?",
            Dialogs.OptionDialogType.YES_NO,
            object : OptionDialogAdapter() {
                override fun yes() {
                    val texture = Textures.get(name)
                    val files = ArrayList<FileHandle>()
                    if (texture is AnimatedTextureSprite) {
                        texture.getTextureList().forEach { files.add(folder.child("${it.name}.png")) }
                    } else files.add(folder.child("$name.png"))
                    files.forEach { it.delete() }
                    TextureBuilder().removeTexture(name)
                    generateAtlas(listOf())
                    Textures.deleteTexture(name)
                    refreshTable()
                }
        })
    }

    private fun generateAtlas(additionalTextures: List<FileHandle>) {
        val textureAtlasGenerator = TextureAtlasGenerator(atlasName)
        textureAtlasGenerator.generate(additionalTextures)
        atlas = TextureAtlas(Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures/$atlasName.atlas"))
    }
}
