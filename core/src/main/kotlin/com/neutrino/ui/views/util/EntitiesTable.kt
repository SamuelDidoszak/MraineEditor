package com.neutrino.ui.views.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.entities.Entity
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.views.AddNewEntityView
import com.neutrino.util.Constants.entityList
import com.neutrino.util.UiManagerFactory

class EntitiesTable(
    private val allowEntityAddition: Boolean,
    private val search: Search<EntityButton>? = null,
    override val callback: (data: String) -> Unit): VisTable(), Callback<String> {

    private val entityButtonList = ArrayList<EntityButton>()

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

    private fun refreshTable() {
        clearChildren()
        initializeTable()
        fillEntityListTable()
    }

    fun displaySearchResults(searchResults: List<EntityButton>) {
        clearChildren()
        initializeTable(searchResults.size)
        fillEntityListTable(searchResults)
    }

    private fun initializeTable(containerCount: Int = entityList.size) {
        fun addAddButtonContainer() {
            val container = Container<Actor>()
            val addButton = TextureButton(Textures.get("addButtonTexture"))
            addButton.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    addNewEntityView()
                }
            })
            container.actor = addButton
            container.actor.setSize(BUTTON_WIDTH, BUTTON_WIDTH)
            container.top()
            add(container).size(BUTTON_WIDTH, BUTTON_HEIGHT).space(0f).pad(0f)
        }

        var rows = containerCount / 4 + if (containerCount % 4 != 0) 1 else 0
        var buttonAdded = !allowEntityAddition

        inner@ for (n in 0 until rows) {
            for (i in 0 until 4) {
                val cellNumber = n * 4 + i
                if (cellNumber == containerCount && !buttonAdded) {
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
        left()
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
                callback.invoke(entity.name)
            }
        })
        entityButtonList.add(entityButton)
        search?.data?.add(Pair(entityButton, entity.name))
        return entityButton
    }

    private fun fillEntityListTable(entityButtons: List<EntityButton>? = null) {
        val entities = entityButtons ?: entityButtonList
        for (i in 0 until entities.size) {
            val container = (children[i] as Container<*>)
            val entityButton = entities[i]
            container.actor = entityButton
        }
    }

    private fun addNewEntityView() {
        UiManagerFactory.getUI().setLeftPanel(AddNewEntityView {
            newEntityAdded(it)
        })
    }

    private fun newEntityAdded(entity: Entity) {
        entityList.add(entity)
        addEntityButton(entity)
        refreshTable()
    }
}
