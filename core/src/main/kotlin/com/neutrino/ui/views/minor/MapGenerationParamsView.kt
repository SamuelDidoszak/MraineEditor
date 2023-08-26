package com.neutrino.ui.views.minor

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.util.UiManagerFactory
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTextButton
import ktx.scene2d.vis.visTextField
import kotlin.random.Random

class MapGenerationParamsView: VisTable() {

    private val rngTextField = scene2d.visTextField { onChange {
        val seed = try {
            text.toLong()
        } catch (e: NumberFormatException) {
            getRandomSeed()
        }
        UiManagerFactory.getUI().getEditorGeneration().seed = seed
        UiManagerFactory.getUI().generateMap()
        println("Changed!")
    }}

    init {
        val rngTable = VisTable()
        rngTable.add(rngTextField).growX()
        rngTable.add(scene2d.visTextButton("random") { onClick {
            rngTextField.text = getRandomSeed().toString()
            rngTextField.fire(ChangeEvent())
        }}).expandX()
        rngTable.add(scene2d.visTextButton("generate") { onClick {
            UiManagerFactory.getUI().generateMap()
        }})

        val mapSizeTable = VisTable()
        mapSizeTable.add(scene2d.visLabel("xSize: ")).expandX().center()
        mapSizeTable.add(scene2d.visTextField("100") { onChange {
            val xSize = try {
                if (text.toInt() < 10)
                    10
                else
                    text.toInt()
            } catch (e: NumberFormatException) {
                100
            }
            println("erntiers")
            UiManagerFactory.getUI().getEditorGeneration().mapXSize = xSize
            UiManagerFactory.getUI().generateMap()
        } }).width(60f).expandX().center()
        mapSizeTable.add(scene2d.visLabel("ySize: ")).expandX().center()
        mapSizeTable.add(scene2d.visTextField("100") { onChange {
            val ySize = try {
                if (text.toInt() < 10)
                    10
                else
                    text.toInt()
            } catch (e: NumberFormatException) {
                100
            }
            println("erntiers")
            UiManagerFactory.getUI().getEditorGeneration().mapYSize = ySize
            UiManagerFactory.getUI().generateMap()
        } }).width(60f).expandX().center()

        add(rngTable).growX().pad(16f).row()
        add(mapSizeTable).growX().padLeft(16f).padRight(16f).row()
    }

    private fun getRandomSeed(): Long {
        return Random(Random.nextLong()).nextLong()
    }
}
