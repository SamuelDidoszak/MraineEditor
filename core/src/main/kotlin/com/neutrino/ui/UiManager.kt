package com.neutrino.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.neutrino.ui.views.EntitiesView

class UiManager(private val stage: Stage) {

    private val leftTable = LeftTable()

    init {
        stage.addActor(leftTable)
        leftTable.setSize(stage.width * 1/4f + 34f, stage.height)
//        leftTable.setWindow(AddNewEntityView())
        leftTable.setWindow(EntitiesView())
    }


//    init {
//        val container = VisTable()
//        container.add(leftTable).width(stage.width * 1/4f + 34f).height(stage.height)
//        container.width = stage.width * 1/4f + 34f
//        container.height = stage.width
//        stage.addActor(container)
//        container.setPosition(0f, 0f)
//        container.pack()
////        stage.addActor(leftTable)
////        leftTable.setSize(stage.width * 1/4f, stage.height)
//        leftTable.setWindow(AddNewEntityView())
//    }
}
