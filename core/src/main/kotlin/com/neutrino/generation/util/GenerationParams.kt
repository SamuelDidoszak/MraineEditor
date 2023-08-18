package com.neutrino.generation.util

import com.neutrino.entities.Entity
import com.neutrino.generation.MapTagInterpretation
import kotlin.random.Random

data class GenerationParams(
    val interpretedTags: MapTagInterpretation,
    val rng: Random,
    var map: List<List<MutableList<Entity>>>
)
