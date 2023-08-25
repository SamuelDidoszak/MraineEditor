package com.neutrino.ui.views.minor.util

import com.neutrino.generation.Generator

interface HasGenerator {

    var generatorName: String

    fun getGenerator(): Generator

    fun generateString(): String
}
