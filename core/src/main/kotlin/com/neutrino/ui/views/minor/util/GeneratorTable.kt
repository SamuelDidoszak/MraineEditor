package com.neutrino.ui.views.minor.util

import com.badlogic.gdx.graphics.Color
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.generation.Generator
import com.neutrino.generation.Generators
import com.neutrino.ui.LeftTable
import com.neutrino.ui.elements.DeleteButton
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel

class GeneratorTable(
    override var generatorName: String,
    onDelete: (generator: GeneratorTable) -> Unit
): VisTable(), HasGenerator {

    private val generator = Generators.get(generatorName)
    private val mainLabel = VisLabel("MAIN")

    init {
        add(scene2d.visLabel(generatorName)).growX().padLeft(16f).left().uniform()
        add(mainLabel).expandX().center().uniform()
        if (!generator.main)
            mainLabel.color = Color(0.4f, 0.4f, 0.4f, 1f)
        add(scene2d.visLabel("Priority: ${generator.priority}")).padRight(16f).expandX().right().uniform()
        add(DeleteButton{ onDelete.invoke(this@GeneratorTable) }).right().top()
    }

    override fun getGenerator(): Generator {
        return generator
    }

    override fun generateString(): String {
        TODO("Not yet implemented")
    }

    override fun getPrefWidth(): Float {
        return LeftTable.WIDTH - 6
    }
}
