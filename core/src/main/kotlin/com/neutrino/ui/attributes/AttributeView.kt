package com.neutrino.ui.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.CollapsibleWidget

abstract class AttributeView(internal val table: Table): CollapsibleWidget(table) {

    abstract val attributeName: String

    abstract fun generateString(): String
    open fun onSaveAction() {}
    open fun validateAttribute(): Boolean {
        return true
    }
//
//    override fun setWidth(width: Float) {
//        println("called")
//        super.setWidth(width)
//        table.width = width
//        println("table ${table.width}")
//    }

    fun getCollapseListener(): ChangeListener {
        return object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                isCollapsed = !isCollapsed
            }
        }
    }
}
