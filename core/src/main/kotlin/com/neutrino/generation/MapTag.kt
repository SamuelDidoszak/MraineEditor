package com.neutrino.generation

import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.util.EntityName
import kotlin.reflect.KClass

data class MapTag(
    val entityIdentities: List<EntityIdentity>,
    val mapGenerators: List<Pair<Float, KClass<out GenerationAlgorithm>>>,
    val characterList: List<EntityName>,
    val itemList: List<Pair<Float, EntityName>>,
    val generationParams: GenerationParams,
    val isModifier: Boolean
)
