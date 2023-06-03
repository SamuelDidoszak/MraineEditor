package com.neutrino

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.scene2d.Scene2DSkin
import ktx.script.KotlinScriptEngine

class Main: ApplicationAdapter() {
    private lateinit var uiStage: Stage

    override fun create() {
        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/uiskin.json"))
        uiStage = Stage(FitViewport(1920f, 1080f))

        val scriptEngine = KotlinScriptEngine()
        scriptEngine.evaluate(Gdx.files.internal("core/AddEntities.kts"))
    }

    override fun render() {
        Gdx.gl.glClearColor(0.24f, 0.24f, 0.24f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        uiStage.act(Gdx.graphics.deltaTime)
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height)
    }

    override fun dispose() {
        uiStage.batch!!.dispose()
    }
}
