package com.neutrino.generation

import com.neutrino.generation.util.GenerationParams

object Generators {

    private val generatorMap: HashMap<String, Generator> = HashMap()
    private val DEFAULT_PRIORITY = 1

    fun add(name: String, generator: Generator) {
        generatorMap[name] = generator
    }

    fun add(name: String, generator: (params: GenerationParams) -> Unit) {
        generatorMap[name] = Generator(false, DEFAULT_PRIORITY, generator)
    }

    fun get(name: String): Generator {
        return generatorMap[name]!!
    }
}
