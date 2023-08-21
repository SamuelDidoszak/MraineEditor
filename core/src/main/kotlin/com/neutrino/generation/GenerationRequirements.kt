package com.neutrino.generation

import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityId
import com.neutrino.util.EntityName

object GenerationRequirements {

    private val entityDefaults: HashMap<EntityId, List<EntityPositionRequirement>> = HashMap()
    private val identityDefaults: HashMap<Identity, List<EntityPositionRequirement>> = HashMap()

    fun nearWall(canBlockPassage: Boolean = false): List<EntityPositionRequirement> {
        var requirementList = listOf(
            EntityPositionRequirement(EntityPositionRequirementType.OR, Identity.Wall(),
                listOf(2, 4, 6, 8))
        )

        if (!canBlockPassage) {
            requirementList = requirementList.plus(listOf(
                EntityPositionRequirement(EntityPositionRequirementType.NAND, Identity.Wall(),
                    listOf(4, 6)),
                EntityPositionRequirement(EntityPositionRequirementType.NAND, Identity.Wall(),
                    listOf(2, 8))
            ))
        }
        return requirementList
    }

    fun add(entity: EntityId, requirements: List<EntityPositionRequirement>) {
        entityDefaults[entity] = requirements
    }

    fun add(entity: EntityName, requirements: List<EntityPositionRequirement>) {
        entityDefaults[Entities.getId(entity)] = requirements
    }

    fun add(identity: Identity, requirements: List<EntityPositionRequirement>) {
        identityDefaults[identity] = requirements
    }

    fun get(entity: EntityName): List<EntityPositionRequirement> {
        return entityDefaults[Entities.getId(entity)]!!
    }

    fun get(entity: EntityId): List<EntityPositionRequirement> {
        return entityDefaults[entity]!!
    }

    fun get(identity: Identity): List<EntityPositionRequirement> {
        return identityDefaults[identity]!!
    }
}
















