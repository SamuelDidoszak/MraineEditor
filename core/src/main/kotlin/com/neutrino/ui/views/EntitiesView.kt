package com.neutrino.ui.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.entities.Entity
import com.neutrino.textures.TextureSprite
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.util.Constants.entityList
import com.neutrino.util.UiManagerFactory

class EntitiesView: VisTable() {

    private val entityButtonList = ArrayList<EntityButton>()

    private val addImage = TextureSprite(
        TextureAtlas.AtlasRegion(
        Texture(Gdx.files.internal("AddButton96.png")), 0, 0, 96, 96))

    private val nameX1Size = true
    private val nameHeight = (if (nameX1Size) 25 else 43) * 2
    private val BUTTON_WIDTH = 128f
    private val BUTTON_HEIGHT = BUTTON_WIDTH + nameHeight

    init {
        this.setFillParent(false)
        clip(true)
        initializeTable()
        initializeEntityButtons()
        fillEntityListTable()
    }

    private fun refreshTable(containerCount: Int = entityList.size) {
        clearChildren()
        initializeTable(containerCount)
        fillEntityListTable()
    }

    private fun initializeTable(containerCount: Int = entityList.size) {
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

        var rows = containerCount / 4 + if (containerCount % 4 != 0) 1 else 0
        var buttonAdded = false

        inner@ for (n in 0 until rows) {
            for (i in 0 until 4) {
                val cellNumber = n * 4 + i
                if (cellNumber == containerCount) {
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
        if (!buttonAdded)
            addAddButtonContainer()

        top()
        pack()
        layout()
    }

    private fun initializeEntityButtons() {
        for (i in 0 until entityList.size) {
            addEntityButton(entityList[i])
        }
    }

    private fun addEntityButton(entity: Entity): EntityButton {
        val entityButton = EntityButton(entity, nameX1Size)
        entityButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        entityButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                println(entity.name)
            }
        })
        entityButtonList.add(entityButton)
        return entityButton
    }

    private fun fillEntityListTable() {
        for (i in 0 until entityButtonList.size) {
            val container = (children[i] as Container<*>)
            val entityButton = entityButtonList[i]
            container.actor = entityButton
        }
    }

    private fun addNewEntityView() {
        UiManagerFactory.getUI().setLeftPanel(AddNewEntityView() {
            assert(it is Entity)
            newEntityAdded(it as Entity)
        })
    }

    private fun newEntityAdded(entity: Entity) {
        entityList.add(entity)
        addEntityButton(entity)
        refreshTable()
    }
}
