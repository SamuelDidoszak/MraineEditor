package com.neutrino.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.neutrino.ui.elements.AddNewEntityView

class UiManager(private val stage: Stage) {

    private val leftTable = LeftTable()

    init {
        stage.addActor(leftTable)
        leftTable.setSize(stage.width * 1/4f, stage.height)
        leftTable.setWindow(AddNewEntityView())
    }
}
