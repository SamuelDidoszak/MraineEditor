package com.neutrino.ui.views.minor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.generation.Generators
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.views.minor.util.GeneratorTable
import com.neutrino.ui.views.minor.util.HasGenerator
import com.neutrino.util.remove
import ktx.actors.onClick
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.subMenu

class GeneratorsView: AttributeView(VisTable()) {

    override val attributeName: String = "Generators"
    private val generators = ArrayList<HasGenerator>()
    private val generatorsToSave = ArrayList<AddGeneratorView>()
    private val addButton = VisTextButton("+")

    init {
        table.add(addButton).padTop(16f).padLeft(16f).left().row()
        addButton.onClick { addGeneratorMenu(addButton) }
    }

    private fun addGeneratorTable(name: String) {
        val generatorTable = GeneratorTable(name) {
            generators.remove(it)
            table.remove(it)
        }
        generators.add(generatorTable)
        table.remove(addButton)
        table.add(generatorTable).growX().padTop(16f).row()
        table.add(addButton).left().padLeft(16f).padTop(16f).row()
    }

    private fun addNewGenerator() {
        val newGenerator = AddGeneratorView {
            generators.remove(it)
            generatorsToSave.remove(it)
            table.remove(it)
        }
        generators.add(newGenerator)
        generatorsToSave.add(newGenerator)
        table.remove(addButton)
        table.add(newGenerator).growX().padTop(32f).padBottom(16f).row()
        table.add(addButton).left().padTop(16f).padLeft(16f).row()
    }

    private fun addGeneratorMenu(actor: Actor) {
        val menu = PopupMenu()
        menu.menuItem("Add new").apply { onClick { addNewGenerator() } }
        Generators.getAssociations().forEach { association ->
            menu.menuItem(association.key).also {
                it.subMenu()
                for (generator in association.value) {
                    it.subMenu.menuItem(generator).apply { onClick { addGeneratorTable(generator) } }
                }
            }
        }
        menu.showMenu(stage, actor)
    }

    override fun generateString(): String {
        TODO("Not yet implemented")
    }
}



















