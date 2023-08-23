package com.neutrino.ui.generators.methods

import com.neutrino.generation.algorithms.GenerationAlgorithm
import ktx.scene2d.scene2d
import ktx.scene2d.vis.visLabel

class GeneratorMethodAddEntityView: GeneratorMethodView() {

    init {
        add(scene2d.visLabel("Add Entity")).growX().left()
        setDebug(true, true)
    }

    override fun invokeMethod(generator: GenerationAlgorithm) {
        TODO("Not yet implemented")
    }
}
