package com.neutrino

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.VisUI
import com.neutrino.entities.Entities
import com.neutrino.ui.UiManager
import com.neutrino.util.Constants.entityList
import com.neutrino.util.Constants.scriptEngine
import com.neutrino.util.UiManagerFactory
import ktx.scene2d.Scene2DSkin

class Main: ApplicationAdapter() {
    private lateinit var uiStage: Stage
    private lateinit var uiManager: UiManager

    override fun create() {
        scriptEngine.importAll(getImportList())
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddTextures.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddEditorTextures.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddEntities.kts"))

        initializeEntities()

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/uiskin.json"))
        VisUI.load(VisUI.SkinScale.X2)
        uiStage = Stage(FitViewport(1920f, 1080f))
        uiManager = UiManager(uiStage)
        UiManagerFactory.registerUiManager(uiManager)
        Gdx.input.inputProcessor = uiStage
    }

    private fun getImportList(): List<String> {
        return listOf("com.neutrino.entities.*", "com.neutrino.entities.attributes.*",
//            "com.neutrino.entities.callables.*", "com.neutrino.entities.util.*",
            "com.neutrino.textures.Textures", "com.neutrino.textures.TextureSprite",
            "com.neutrino.textures.AnimatedTextureSprite",
//            "com.neutrino.textures.Light", "com.neutrino.textures.LightSources",
//            "com.badlogic.gdx.graphics.Color", "com.badlogic.gdx.graphics.g2d.TextureAtlas",
//            "com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion", "com.badlogic.gdx.utils.Array",

            )
    }

    private fun initializeEntities() {
        try {
            var id = 0
            while (true) {
                entityList.add(Entities.new(id))
                id++
            }
        } catch (_: Exception) {}
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
