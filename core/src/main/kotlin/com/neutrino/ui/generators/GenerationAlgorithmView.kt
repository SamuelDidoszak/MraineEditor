package com.neutrino.ui.generators

import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.generation.util.GenerationParams
import com.neutrino.generation.util.ModifyMap

abstract class GenerationAlgorithmView: VisTable() {

    var sizeX: Int? = null
    var sizeY: Int? = null
    var modifyBaseMap: ModifyMap? = null

    abstract fun getGenerationAlgorithm(params: GenerationParams): GenerationAlgorithm

    abstract fun generateString(): String

    fun getModifyMapView(): VisTable {
        return VisTable()
    }
}
