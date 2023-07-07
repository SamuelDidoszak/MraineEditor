package com.neutrino.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.tools.texturepacker.TexturePacker

class TextureAtlasGenerator(private var atlasName: String) {

    private val settings = TexturePacker.Settings()
    private var textureFolder = setTextureFolder()

    init {
        settings.legacyOutput = false
        settings.silent = true
    }

    fun generate(files: List<FileHandle>, name: String = atlasName) {
        if (atlasName != name) {
            atlasName = name
            textureFolder = setTextureFolder()
        }
        copyFiles(files)
        removePreviousAtlas()

        val texturePacker = TexturePacker(textureFolder.file(), settings)
        for (file in textureFolder.list()) {
            texturePacker.addImage(file.file())
        }
        texturePacker.pack(Gdx.files.local("assets/textures/").file(), name)
    }

    private fun setTextureFolder(): FileHandle {
        val folder = Gdx.files.local("textureSources/$atlasName/")
        folder.mkdirs()
        return folder
    }

    private fun copyFiles(files: List<FileHandle>) {
        files.forEach { println(it.name()) }
        for (file in files) {
            file.copyTo(textureFolder)
        }
    }

    private fun removePreviousAtlas() {
        Gdx.files.local("assets/textures/$atlasName.png").delete()
        Gdx.files.local("assets/textures/$atlasName.atlas").delete()
    }
}
