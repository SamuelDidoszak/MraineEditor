package com.neutrino.ui.views.minor

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.neutrino.generation.Generator
import com.neutrino.ui.LeftTable
import com.neutrino.ui.elements.DeleteButton
import com.neutrino.ui.elements.TextField
import com.neutrino.ui.generators.GenerationAlgorithmView
import com.neutrino.ui.generators.SquidGenerationAlgorithmView
import com.neutrino.ui.generators.methods.GeneratorMethodAddEntityView
import com.neutrino.ui.generators.methods.GeneratorMethodView
import com.neutrino.ui.views.minor.util.HasGenerator
import com.neutrino.util.remove
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.*
import kotlin.reflect.full.createInstance

class AddGeneratorView(
    private val onDelete: (generator: AddGeneratorView) -> Unit
): VisTable(), HasGenerator {

    override var generatorName = ""
    var main = true
    var priority: Int = 1

    override var generator: Generator = Generator(main, priority) {}
    private val generators: ArrayList<Pair<GenerationAlgorithmView, ArrayList<GeneratorMethodView>>> = ArrayList()
    private val generationAlgorithms = mapOf(
        "SquidGenerationAlgorithm" to SquidGenerationAlgorithmView::class
    )
    private val generatorMethods = mapOf(
        "Add Entity" to GeneratorMethodAddEntityView::class,
    )
    private val addGenerator: VisTable

    init {
        left()
        addHeader()
        addGenerator = VisTable()
        addGenerator.add(scene2d.visTextButton("+").apply { onClick {
            scene2d.popupMenu {
                generationAlgorithms.keys.forEach {
                    menuItem(it).onClick {
                        addGenerationAlgorithm(it)
                    }}}.showMenu(stage, this)
        }}).padLeft(16f).padRight(16f).padTop(16f).left()
        addGenerator.add(scene2d.visLabel("Add generation algorithm")).expandX().left()
        add(addGenerator).growX().row()
    }

    private fun addGenerationAlgorithm(algorithmName: String) {
        remove(addGenerator)
        val generatorView = generationAlgorithms[algorithmName]!!.createInstance()
        generators.add(generatorView to ArrayList())
        val generatorTable = VisTable()
        val titleTable = VisTable()
        titleTable.background = VisTextField("").style.background
        titleTable.add(scene2d.visLabel(algorithmName)).expandX().center()
        generatorTable.add(titleTable).padBottom(16f)
        generatorTable.add(DeleteButton {
            generators.find { it.first == generatorView }!!.second.forEach {
                remove(it) }
            generators.removeIf { it.first == generatorView }
            remove(generatorTable)
        }).right().top().row()
        generatorTable.add(generatorView).growX().row()

        val addMethodTable = VisTable()
        val addButton = VisTextButton("+")
        addButton.onClick {
            scene2d.popupMenu {
                generatorMethods.keys.forEach {
                    menuItem(it).onClick {
                        val methodView = generatorMethods[it]!!.createInstance()
                        generatorTable.remove(addMethodTable)
                        generatorTable.add(methodView).padTop(16f).padLeft(16f).growX().row()
                        generatorTable.add(addMethodTable).expandX().left().row()
                        methodView.add(DeleteButton {
                            generators.find { it.first == generatorView }?.second?.remove(methodView)
                            generatorTable.remove(methodView)
                        }).right().top()
            }}}.showMenu(stage, this)
        }
        addMethodTable.add(addButton).padLeft(16f).padRight(16f).padTop(16f)
        addMethodTable.add(scene2d.visLabel("Add method"))
        generatorTable.add(addMethodTable).expandX().left().row()

        add(generatorTable).growX().padTop(16f).row()
        add(addGenerator).growX().row()
    }

    private fun addHeader() {
        val paramsTable = VisTable()
        paramsTable.add(scene2d.visTextField { onChange { generatorName = text } }).padLeft(16f).padRight(16f)
        paramsTable.add(scene2d.visLabel("MAIN") { onClick {
            main = !main
            color = if (main)
                Color.valueOf("ffffffff")
            else
                Color(0.4f, 0.4f, 0.4f, 1f)
        } }).expandX().center()
        paramsTable.add(scene2d.visLabel("Priority: ")).padRight(16f).expandX().right()
        val priorityTextField = TextField(priority.toString())
        priorityTextField.width = 40f
        paramsTable.add(priorityTextField).left()
        paramsTable.add(DeleteButton { onDelete.invoke(this@AddGeneratorView) }).right().top()
        add(paramsTable).growX().row()
    }

    override fun getPrefWidth(): Float {
        return LeftTable.WIDTH - 6f
    }
}





















