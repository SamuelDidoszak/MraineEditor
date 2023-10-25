package com.neutrino.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.VisUI
import com.neutrino.entities.Entity
import ktx.script.KotlinScriptEngine

object Constants {

    val whitePixel = Texture(Gdx.files.internal("whitePixel.png"))

    val fileChooser = FileChooser()

    val entityList = ArrayList<Entity>()

    val SCALE = 3f
    val SCALE_INT = SCALE.toInt()
    val TILE_SIZE = 16 * SCALE
    val TILE_SIZE_INT = 16 * SCALE_INT

    val VisUIX1Skin = Skin(VisUI.SkinScale.X1.skinFile)
    val scriptEngine = KotlinScriptEngine()

    val seed = 2137
}
