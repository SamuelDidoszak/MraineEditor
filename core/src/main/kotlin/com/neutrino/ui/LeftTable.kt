package com.neutrino.ui

import com.badlogic.gdx.scenes.scene2d.Group
import com.kotcrab.vis.ui.widget.VisWindow

class LeftTable: Group() {

    fun setWindow(window: VisWindow) {
        addActor(window)
        window.setPosition(0f, 0f)
        window.pack()
        window.setSize(width, height)
        println("Window: ${window.width}")
    }

}
