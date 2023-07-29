package com.neutrino.ui.editor

import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.entities.Entities
import com.neutrino.textures.LevelDrawer

class Editor: Group() {

    private val levelDrawer = LevelDrawer()
    val editorStage = EditorStage(levelDrawer)

    init {
        editorStage.addActor(levelDrawer)
        for (y in levelDrawer.map.indices) {
            for (x in levelDrawer.map[0].indices)
                levelDrawer.map[y][x].add(Entities.new("DungeonFloor"))
        }
        levelDrawer.initializeTextures()
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        editorStage.viewport.setScreenSize(width.toInt(), height.toInt())
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        editorStage.viewport.setScreenPosition(x.toInt(), y.toInt())
    }
}
