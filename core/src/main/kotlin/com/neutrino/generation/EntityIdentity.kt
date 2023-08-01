package com.neutrino.generation

import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityName
import kotlin.random.Random

class EntityIdentity(
    val identity: Identity,
    val entities: List<EntityName>
) {
    fun getRandomEntityName(rng: Random): EntityName {
        if (entities.size == 1)
            return entities[0]
        val randVal = rng.nextFloat() * 100f
        return entities[(randVal / entities.size).toInt()]
    }
}
