package com.neutrino

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.VisUI
import com.neutrino.entities.Entities
import com.neutrino.ui.UiManager
import com.neutrino.util.Constants.entityList
import com.neutrino.util.Constants.scriptEngine
import com.neutrino.util.UiManagerFactory
import ktx.scene2d.Scene2DSkin

class Main: ApplicationAdapter() {
    private lateinit var uiManager: UiManager

    override fun create() {
        scriptEngine.importAll(getImportList())
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddTextures.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddEditorTextures.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddEntities.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddTilesets.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddGenerators.kts"))
        scriptEngine.evaluate(Gdx.files.local("assets/core/AddGenerationRequirements.kts"))

        initializeEntities()

        Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("ui/uiskin.json"))
        VisUI.load(VisUI.SkinScale.X2)
        uiManager = UiManager()
        UiManagerFactory.registerUiManager(uiManager)
    }

    private fun getImportList(): List<String> {
        return listOf("com.neutrino.entities.*",
            "com.neutrino.entities.attributes.*",
            "com.neutrino.entities.attributes.character.*",
            "com.neutrino.entities.attributes.map.*",
//            "com.neutrino.entities.callables.*", "com.neutrino.entities.util.*",
            "com.neutrino.textures.Textures", "com.neutrino.textures.TextureSprite",
            "com.neutrino.textures.AnimatedTextureSprite",
            "com.neutrino.generation.NameOrIdentity",
            "com.neutrino.generation.Tilesets",
            "com.neutrino.generation.Generators",
            "com.neutrino.generation.algorithms.*",
            "squidpony.squidgrid.mapping.styled.TilesetType",
            "com.neutrino.generation.*",
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
        uiManager.render()
    }

    override fun resize(width: Int, height: Int) {
        uiManager.resize(width, height)
    }

    override fun dispose() {
        deleteTemporaryFiles()
        uiManager.dispose()
    }

    private fun deleteTemporaryFiles() {
        val folder = Gdx.files.local("textureSources/temp")
        folder.deleteDirectory()
    }
}
