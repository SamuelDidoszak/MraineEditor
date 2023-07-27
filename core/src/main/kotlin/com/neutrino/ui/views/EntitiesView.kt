package com.neutrino.ui.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.textures.TextureSprite
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.Constants.entityList
import com.neutrino.util.UiManagerFactory
import ktx.scene2d.container
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTable

class EntitiesView: VisTable() {

    private val addImage = TextureSprite(
        TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))

    private val nameX1Size = true
    private val nameHeight = (if (nameX1Size) 25 else 43) * 2
    private val BUTTON_WIDTH = 128f
    private val BUTTON_HEIGHT = BUTTON_WIDTH + nameHeight

    init {
        fun addAddButtonContainer() {
            val container = Container<Actor>()
            val addButton = TextureButton(addImage)
            addButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    addNewEntityView()
                }
            })
            container.actor = addButton
            container.actor.setSize(BUTTON_WIDTH, BUTTON_WIDTH)
            container.top()
            add(container).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f).top()
        }

        var rows = entityList.size / 4 + if (entityList.size % 4 != 0) 1 else 0
        var buttonAdded = false
        this.setFillParent(false)
        clip(true)

        inner@ for (n in 0 until rows) {
            for (i in 0 until 4) {
                val cellNumber = n * 4 + i
                if (cellNumber == entityList.size) {
                    addAddButtonContainer()
                    buttonAdded = true
                    break
                }
                val container = Container<Actor>()
                container.name = (cellNumber).toString()
                add(container).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
            }
            row()
        }
        if (!buttonAdded) {
            addAddButtonContainer()
            buttonAdded = true
        }

        top()
        pack()
        layout()

        fillEntityListTable()
    }

    private fun fillEntityListTable() {
        for (i in 0 until entityList.size) {
            val container = (children[i] as Container<*>)
            val entityButton = EntityButton(entityList[i], nameX1Size)
            entityButton.setSize(container.width, container.height)
            container.actor = entityButton
            entityButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    println(entityList[i].name)
                }
            })
        }
    }

    private fun getEntityListTable(): VisTable {
        fun getAddButton(): TextureButton {
            val addButton = TextureButton(addImage)
            addButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    addNewEntityView()
                }
            })
            return addButton
        }
        var buttonAdded = false

        var rows = entityList.size / 4 + if (entityList.size % 4 != 0) 1 else 0

        val table = scene2d.visTable {
            this.setFillParent(false)
            clip(true)
            inner@ for (n in 0 until rows) {
                for (i in 0 until 4) {
                    val cellNumber = n * 4 + i
                    if (cellNumber == entityList.size) {
                        add(container {
                            actor = getAddButton()
                            actor.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
                        }).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
                        buttonAdded = true
                        break
                    }
                    add(container {
                        name = (cellNumber).toString()
                    }).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
                }
                row()
            }
            if (!buttonAdded) {
                add(container {
                    actor = getAddButton()
                    actor.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
                }).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
                buttonAdded = true
            }
        }
        table.top()
        table.pack()
        table.layout()

        return table
    }

    private fun addNewEntityView() {
        UiManagerFactory.getUI().setLeftPanel(AddNewEntityView())
    }
}
