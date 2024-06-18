package com.neutrino.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.textures.Shaders
import com.neutrino.ui.editor.Editor
import com.neutrino.ui.editor.EditorGeneration
import com.neutrino.ui.views.EntitiesView
import com.neutrino.ui.views.TagsView
import com.neutrino.ui.views.TexturesView
import com.neutrino.ui.views.util.UiTab
import ktx.actors.onClick
import ktx.actors.onEnter
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTextButton
import java.util.*

class UiManager() {
    private val uiStage: Stage = Stage(FitViewport(1920f, 1080f),
        SpriteBatch(100, Shaders.fragmentAlphas))
    private val leftTable = LeftTable()
    private val tabs = getTabs()
    private val editor = Editor()
    private val panelHistory = LinkedList<Pair<VisTable, UiTab>>()
    private var activeTab = UiTab.EntitiesTab
    private val defaultTabWindows = EnumMap<UiTab, VisTable>(mapOf(
        UiTab.EntitiesTab to EntitiesView(true, true) {},
        UiTab.TagsTab to TagsView(),
        UiTab.ItemsTab to VisTable(),
        UiTab.CharactersTab to VisTable(),
        UiTab.TexturesTab to TexturesView()
    ))
    private var entitiesTabHoverLength = 0f

    init {
        uiStage.addActor(tabs)
        tabs.setPosition(258f, uiStage.height - 20)
        uiStage.addActor(leftTable)
        leftTable.setSize(LeftTable.WIDTH, uiStage.height - 42)
        editor.setPosition(leftTable.width, 0f)
        setLeftPanel(defaultTabWindows[UiTab.EntitiesTab]!!, UiTab.EntitiesTab)
        setInputMultiplexer()
    }

    fun getEditorGeneration(): EditorGeneration {
        return editor.editorGeneration
    }

    fun generateMap() {
        getEditorGeneration().generateMap()
    }

    private fun getTabs(): VisTable {
        val tabWidth = LeftTable.WIDTH / 4
        val tabTable = VisTable()
        tabTable.add(scene2d.visTextButton("Entities", "toggle") {
            addListener(onClick { setActiveTab(UiTab.EntitiesTab) })
            addListener(object : ClickListener() {
                override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
                    entitiesTabHoverLength += Gdx.graphics.deltaTime
                    if (entitiesTabHoverLength > 0.16f) {
                        showTexturesTab(it)
                        entitiesTabHoverLength = -500f
                    }
                    return super.mouseMoved(event, x, y)
                }

                override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    entitiesTabHoverLength = 0f
                    super.exit(event, x, y, pointer, toActor)
                }
            })
        }).width(tabWidth).fillX()
        tabTable.add(scene2d.visTextButton("Tags", "toggle") {
            addListener(onClick { setActiveTab(UiTab.TagsTab) })
            addListener(onEnter { hideTexturesTab() })
        }).width(tabWidth).left().expandX()
        tabTable.add(scene2d.visTextButton("Items", "toggle") {
            addListener(onClick { setActiveTab(UiTab.ItemsTab) })
        }).width(tabWidth).left().fillX()
        tabTable.add(scene2d.visTextButton("Character", "toggle") {
            addListener(onClick { setActiveTab(UiTab.CharactersTab) })
        }).width(tabWidth).left().expandX()
        return tabTable
    }

    fun setActiveTab(tab: UiTab) {
        changeTabToggle(tab)
        if (activeTab != tab) {
            setLeftPanel(defaultTabWindows[tab]!!, tab)
        }
    }

    private fun changeTabToggle(tab: UiTab) {
        if (activeTab != UiTab.TexturesTab)
            (tabs.getChild(activeTab.ordinal) as VisTextButton).isChecked = false
        if (tab != UiTab.TexturesTab)
            (tabs.getChild(tab.ordinal) as VisTextButton).isChecked = true
    }

    fun setLeftPanel(window: VisTable, tab: UiTab) {
        leftTable.setWindow(window)
        panelHistory.add(window to tab)
        changeTabToggle(tab)
        activeTab = tab
    }

    fun previousPanel() {
        if (panelHistory.size < 2)
            return
        panelHistory.removeLast()
        leftTable.setWindow(panelHistory.last.first)
        changeTabToggle(panelHistory.last.second)
    }

    fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height)
        editor.editorStage.viewport.update(width, height)
        leftTable.setPosition(0f, 0f)
        editor.setPosition(LeftTable.WIDTH * (width / 1920f), 0f)
    }

    private fun setInputMultiplexer() {
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(uiStage)
        inputMultiplexer.addProcessor(editor.editorStage)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    fun render() {
        editor.editorStage.viewport.apply()
        editor.editorStage.act(Gdx.graphics.deltaTime)
        editor.editorStage.draw()

        uiStage.viewport.apply()
        uiStage.act(Gdx.graphics.deltaTime)
        uiStage.draw()
    }

    fun dispose() {
        uiStage.batch.dispose()
        editor.editorStage.batch.dispose()
    }

    private fun showTexturesTab(actor: Actor) {
        val menu = PopupMenu()
        menu.name = "TexturesTab"
        val menuItem = MenuItem("Textures", object : ChangeListener() {
            override fun changed(p0: ChangeEvent?, p1: Actor?) {
                setActiveTab(UiTab.TexturesTab)
            }
        })
        menuItem.addListener(object : ClickListener() {
            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                hideTexturesTab()
            }
        })
        menu.addItem(menuItem)
        menu.showMenu(uiStage, actor)
    }

    private fun hideTexturesTab() {
        val menu = uiStage.actors.find { it.name == "TexturesTab" } ?: return
        uiStage.actors.removeValue(menu as PopupMenu, true)
        entitiesTabHoverLength = 0f
    }

//    init {
//        val container = VisTable()
//        container.add(leftTable).width(stage.width * 1/4f + 34f).height(stage.height)
//        container.width = stage.width * 1/4f + 34f
//        container.height = stage.width
//        stage.addActor(container)
//        container.setPosition(0f, 0f)
//        container.pack()
////        stage.addActor(leftTable)
////        leftTable.setSize(stage.width * 1/4f, stage.height)
//        leftTable.setWindow(AddNewEntityView())
//    }
}
