package com.neutrino.ui.generators.methods

import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.generation.algorithms.GenerationAlgorithm

abstract class GeneratorMethodView: VisTable() {

    abstract fun addMethod(generator: GenerationAlgorithm)

    abstract fun generateString(): String
}
