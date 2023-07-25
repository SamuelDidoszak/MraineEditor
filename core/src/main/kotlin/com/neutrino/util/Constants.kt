package com.neutrino.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.neutrino.entities.Entity

object Constants {

    val whitePixel = Texture(Gdx.files.internal("whitePixel.png"))

    val fileChooser = FileChooser()

    val entityList = ArrayList<Entity>()

    val SCALE = 3f
    val SCALE_INT = SCALE.toInt()
}
