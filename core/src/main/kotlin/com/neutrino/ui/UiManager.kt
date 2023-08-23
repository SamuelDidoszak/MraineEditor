package com.neutrino.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.ui.editor.Editor
import com.neutrino.ui.views.EntitiesView
import com.neutrino.ui.views.TagsView
import com.neutrino.ui.views.util.UiTab
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visTextButton
import java.util.*

class UiManager() {
    private val uiStage: Stage = Stage(FitViewport(1920f, 1080f))
    private val leftTable = LeftTable()
    private val tabs = getTabs()
    private val editor = Editor()
    private val panelHistory = LinkedList<Pair<VisTable, UiTab>>()
    private var activeTab = UiTab.EntitiesTab
    private val defaultTabWindows = EnumMap<UiTab, VisTable>(mapOf(
        UiTab.EntitiesTab to EntitiesView(true, true) {},
        UiTab.TagsTab to TagsView(),
        UiTab.ItemsTab to VisTable(),
        UiTab.CharactersTab to VisTable()
    ))

    init {
        uiStage.addActor(tabs)
        tabs.setPosition(258f, uiStage.height - 20)
        println(tabs.height)
        uiStage.addActor(leftTable)
        leftTable.setSize(LeftTable.WIDTH, uiStage.height - 42)
        editor.setPosition(leftTable.width, 0f)
        setLeftPanel(defaultTabWindows[UiTab.EntitiesTab]!!, UiTab.EntitiesTab)
        println(leftTable.x)
        setInputMultiplexer()
    }

    private fun getTabs(): VisTable {
        val tabWidth = LeftTable.WIDTH / 4
        val tabTable = VisTable()
        tabTable.add(scene2d.visTextButton("Entities", "toggle") {
            addListener(onClick { setActiveTab(UiTab.EntitiesTab) })
        }).width(tabWidth).fillX()
        tabTable.add(scene2d.visTextButton("Tags", "toggle") {
            addListener(onClick { setActiveTab(UiTab.TagsTab) })
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
        println(activeTab)
        println(tab)
        println()
        (tabs.getChild(activeTab.ordinal) as VisTextButton).isChecked = false
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
