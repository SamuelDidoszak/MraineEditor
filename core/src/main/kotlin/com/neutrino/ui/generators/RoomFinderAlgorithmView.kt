package com.neutrino.ui.generators

import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextField
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.generation.algorithms.RectangleRoomFinderAlgorithm
import com.neutrino.generation.algorithms.RoomFinderAlgorithm
import com.neutrino.generation.util.GenerationParams
import com.neutrino.ui.generators.methods.GeneratorMethodAddEntityView
import com.neutrino.ui.generators.methods.GeneratorMethodView
import com.neutrino.util.UiManagerFactory
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.visLabel
import kotlin.reflect.KClass

class RoomFinderAlgorithmView: GenerationAlgorithmView() {

    private var finderType: FinderType = FinderType.NORMAL

    init {
        val tilesetTypeTable = VisTable()
        tilesetTypeTable.add(scene2d.visLabel("Type: "))
        tilesetTypeTable.add(scene2d.visLabel(finderType.name) {
            onClick { getTilesetTypeMenu(this) }
        })
        tilesetTypeTable.background = VisTextField("").style.background
        add(tilesetTypeTable).padLeft(16f).expandX().left().row()
    }

    private fun getTilesetTypeMenu(label: VisLabel) {
        val menu = PopupMenu()
        FinderType.values().forEach { type ->
            menu.menuItem(type.name).apply { onClick {
                finderType = type
                label.setText(type.name)

                UiManagerFactory.getUI().generateMap()
        } } }
        menu.showMenu(stage, label)
    }

    override fun getGenerationAlgorithm(params: GenerationParams): GenerationAlgorithm {
        return when (finderType) {
            FinderType.RECTANGLE -> {
                RectangleRoomFinderAlgorithm(params, modifyBaseMap = modifyBaseMap)
            }
            FinderType.NORMAL -> {
                RoomFinderAlgorithm(params, modifyBaseMap = modifyBaseMap)
            }
        }
    }

    override fun generateString(): String {
        return when (finderType) {
            FinderType.RECTANGLE -> "RectangleRoomFinderAlgorithm(it)"
            FinderType.NORMAL -> "RoomFinderAlgorithm(it)"
        }
    }

    override fun getMethods(): Map<String, KClass<out GeneratorMethodView>> {
        return when (finderType) {
            FinderType.RECTANGLE -> mapOf()
            FinderType.NORMAL -> mapOf(
                "Add in rooms" to AddInRooms::class,
                "Add in corridors" to AddInCorridors::class,
            )
        }
    }

    private enum class FinderType {
        RECTANGLE,
        NORMAL
    }

    internal class AddInRooms: GeneratorMethodAddEntityView() {

        override fun addMethod(generator: GenerationAlgorithm) {
            if (entity == null || generator !is RoomFinderAlgorithm)
                return
            generator.addInRooms(entity!!.name, getRules(), amount, asPercent, replaceUnderneath)
        }

        override fun generateString(): String {
            val builder = StringBuilder(100)
            builder.append(".addInRooms(\"${entity!!.name}\", ")
            addRulesString(builder)
            addParametersString(builder)
            return builder.toString()
        }
    }

    internal class AddInCorridors: GeneratorMethodAddEntityView() {

        override fun addMethod(generator: GenerationAlgorithm) {
            if (entity == null || generator !is RoomFinderAlgorithm)
                return
            generator.addInCorridors(entity!!.name, getRules(), amount, asPercent, replaceUnderneath)
        }

        override fun generateString(): String {
            val builder = StringBuilder(100)
            builder.append(".addInCorridors(\"${entity!!.name}\", ")
            addRulesString(builder)
            addParametersString(builder)
            return builder.toString()
        }
    }
}
