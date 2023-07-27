package com.neutrino.ui.elements

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisImageButton
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.util.UiManagerFactory

class ViewTitle(title: String, onBackButtonClick: () -> Unit = {
    UiManagerFactory.getUI().previousPanel()
}): VisTable() {

    init {
        val backButton = VisImageButton(VisUI.getSkin().getDrawable("icon-arrow-left"))
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                onBackButtonClick.invoke()
            }
        })
        add(backButton).left()
        add(VisLabel(title)).padLeft(32f).padRight(32f).expandX().center()
    }
}
