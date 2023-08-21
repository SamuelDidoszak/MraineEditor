package com.neutrino.generation

import com.neutrino.generation.util.GenerationParams

class Generator(
    val main: Boolean,
    val priority: Int,
    private val generate: (params: GenerationParams) -> Unit) {

    fun generate(params: GenerationParams) {
        generate.invoke(params)
    }
}
