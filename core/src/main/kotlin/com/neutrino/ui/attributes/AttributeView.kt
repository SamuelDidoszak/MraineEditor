package com.neutrino.ui.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.CollapsibleWidget
import com.kotcrab.vis.ui.widget.VisTable

abstract class AttributeView(internal val table: VisTable): CollapsibleWidget(table) {

    abstract val attributeName: String

    abstract fun generateString(): String
    open fun onSaveAction() {}
    open fun validateAttribute(): Boolean {
        return true
    }

    fun getCollapseListener(): ChangeListener {
        return object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                isCollapsed = !isCollapsed
            }
        }
    }
}
