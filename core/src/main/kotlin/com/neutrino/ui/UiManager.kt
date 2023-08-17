package com.neutrino.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.ui.editor.Editor
import com.neutrino.ui.views.EntitiesView
import java.util.*

class UiManager() {
    private val uiStage: Stage = Stage(FitViewport(1920f, 1080f))
    private val leftTable = LeftTable()
    private val editor = Editor()
    private val panelHistory = LinkedList<VisTable>()

    init {
        uiStage.addActor(leftTable)
        leftTable.setSize(uiStage.width * 1/4f + 34f, uiStage.height)
        editor.setPosition(leftTable.width, 0f)
        setLeftPanel(EntitiesView(true, true) {})
        setInputMultiplexer()
    }

    fun setLeftPanel(window: VisTable) {
        leftTable.setWindow(window)
        panelHistory.add(window)
    }

    fun previousPanel() {
        if (panelHistory.size < 2)
            return
        panelHistory.removeLast()
        leftTable.setWindow(panelHistory.last)
    }

    fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height)
        editor.editorStage.viewport.update(width, height)
        leftTable.setPosition(0f, 0f)
        editor.setPosition((1920 * 1/4f + 34f) * (width / 1920f), 0f)
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
