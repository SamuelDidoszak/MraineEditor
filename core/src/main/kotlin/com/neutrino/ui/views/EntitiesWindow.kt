package com.neutrino.ui.views

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.util.EntityName
import ktx.actors.setScrollFocus

class EntitiesWindow(
    onEntityPickedCallback: (EntityName) -> Unit
): VisWindow("") {

    init {
        isModal = true
        closeOnEscape()
        addCloseButton()

        val container = Container<VisScrollPane>()
        container.actor = getScrollPane(EntitiesView(false, false) {
            onEntityPickedCallback.invoke(it)
            close()
        })
        add(container).expandX().padTop(16f)

        pack()
        height = 1080 * 3/4f
        centerWindow()
    }

    private fun getScrollPane(table: VisTable): VisScrollPane {
        val entitiesScrollPane = VisScrollPane(table)
        entitiesScrollPane.setScrollFocus(true)
        entitiesScrollPane.setScrollingDisabled(true, false)
        entitiesScrollPane.setOverscroll(false, false)
        entitiesScrollPane.setScrollbarsVisible(true)
        entitiesScrollPane.setScrollbarsOnTop(true)
        return entitiesScrollPane
    }
}
