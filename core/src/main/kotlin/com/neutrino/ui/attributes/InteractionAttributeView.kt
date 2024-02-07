package com.neutrino.ui.attributes

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.entities.util.InteractionType
import com.neutrino.ui.elements.VisTableNested

class InteractionAttributeView: AttributeView(VisTable()) {

    override val attributeName: String = "Interaction"
    private val interactionTable = VisTableNested()
    private val interactionList = InteractionType::class.nestedClasses.map { it.simpleName }

    init {
        TableUtils.setSpacingDefaults(table)
        table.pad(16f)
        interactionTable.columnDefaults(0).left()
        interactionTable.left()
        interactionTable.add(getAddButton())
        table.add(interactionTable).left().padLeft(16f).padRight(16f).growX()
    }

    private fun getAddButton(): VisTextButton {
        return VisTextButton("+", object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                interactionTable.width = width - 64f
                val menu = PopupMenu()
                val remainingInteractions = interactionList.minus(interactionTable.getElements().map { (it as? VisTextButton)?.text.toString() }.toSet())
                remainingInteractions.forEach {
                    if (it != null)
                        menu.addItem(interactionMenuItem(it.toString()))
                }
                menu.showMenu(stage, actor)
            }
        })
    }

    private fun interactionMenuItem(name: String): MenuItem {
        return MenuItem(name, object: ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val newInteraction = VisTextButton(name, object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        interactionTable.removeLast()
                        interactionTable.removeNested(actor!!)
                        interactionTable.addNested(getAddButton())
                    }
                })
                interactionTable.removeLast()
                interactionTable.addNested(newInteraction)
                interactionTable.addNested(getAddButton())
            }
        })
    }

    override fun generateString(): String {
        val builder = StringBuilder(100)
        builder.append("InteractionAttribute(arrayListOf(")
        val interactions = interactionTable.getElements().map { (it as? VisTextButton)?.text.toString() }
        for (i in 0 until interactions.size - 1) {
            builder.append("Interaction.${interactions[i]}(), ")
        }
        builder.delete(builder.length - 2, builder.length)
        builder.append("))")
        return builder.toString()
    }

    override fun validateAttribute(): Boolean {
        if (interactionTable.getElements().size == 1)
            return false
        return true
    }
}
