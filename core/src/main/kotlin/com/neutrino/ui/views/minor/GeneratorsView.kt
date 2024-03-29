package com.neutrino.ui.views.minor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.generation.Generator
import com.neutrino.generation.Generators
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.views.minor.util.GeneratorTable
import com.neutrino.ui.views.minor.util.HasGenerator
import com.neutrino.util.UiManagerFactory
import com.neutrino.util.remove
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.separator
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

    fun getGenerators(): List<Generator> {
        return generators.map { it.getGenerator() }
    }

    private fun addGeneratorTable(name: String) {
        val separator = scene2d.separator()
        val generatorTable = GeneratorTable(name) {
            generators.remove(it)
            table.remove(it)
            table.remove(separator)
            UiManagerFactory.getUI().generateMap()
        }
        generators.add(generatorTable)
        table.remove(addButton)
        table.add(generatorTable).growX().padTop(16f).row()
        table.add(separator).growX().padLeft(16f).padRight(16f).padTop(8f).row()
        table.add(addButton).left().padLeft(16f).padTop(16f).row()

        UiManagerFactory.getUI().generateMap()
    }

    private fun addNewGenerator() {
        val separator = scene2d.separator()
        val newGenerator = AddGeneratorView {
            generators.remove(it)
            generatorsToSave.remove(it)
            table.remove(it)
            table.remove(separator)
            UiManagerFactory.getUI().generateMap()
        }
        generators.add(newGenerator)
        generatorsToSave.add(newGenerator)
        table.remove(addButton)
        table.add(newGenerator).growX().padTop(32f).padBottom(16f).row()
        table.add(separator).growX().padLeft(16f).padRight(16f).row()
        table.add(addButton).left().padTop(16f).padLeft(16f).row()

        UiManagerFactory.getUI().generateMap()
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

    fun save() {
        generatorsToSave.forEach {
            it.save()
        }
    }

    override fun generateString(): String {
        TODO("Not yet implemented")
    }
}
