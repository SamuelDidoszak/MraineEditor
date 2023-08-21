package com.neutrino.generation.util

import com.neutrino.generation.EntityPositionRequirement
import com.neutrino.util.EntityId

data class EntityGenerationParams(
    val id: EntityId,
    val requirements: List<EntityPositionRequirement>,
    val amount: Float,
    val asProbability: Boolean = false,
    val replaceUnderneath: Boolean = false
)
