package com.neutrino.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTable
import ktx.actors.setScrollFocus

class LeftTable: VisScrollPane(Actor()) {

    private val background = VisUI.getSkin().get("default", Window.WindowStyle::class.java).background

    companion object {
        val WIDTH = 1920 * 1/4f + 34f
    }

    init {
        setScrollFocus(true)
        setScrollingDisabled(true, false)
        setOverscroll(false, false)
        setScrollbarsVisible(true)
        setScrollbarsOnTop(true)
    }

    fun setWindow(window: VisTable) {
        actor = window
        window.background = background
        window.setPosition(0f, 0f)
        window.top()
        window.setSize(width - 34f, height)
        scrollTo(0f, 10000f, 0f, 0f)
    }

}
