package com.neutrino.generation.algorithms.util

import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.EntityPositionRequirement
import com.neutrino.generation.GenerationRequirements
import com.neutrino.generation.algorithms.RoomFinderAlgorithm
import com.neutrino.generation.util.EntityGenerationParams
import com.neutrino.util.EntityName
import com.neutrino.util.id

interface RoomFinderMethods {

    val thisAlgorithm: RoomFinderAlgorithm
    val roomEntities: ArrayList<EntityGenerationParams>
    val corridorEntities: ArrayList<EntityGenerationParams>


    fun addInRooms(name: EntityName, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        roomEntities.add(EntityGenerationParams(id, GenerationRequirements.get(id), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInRooms(name: EntityName, entityRequirement: EntityName, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        roomEntities.add(EntityGenerationParams(id, GenerationRequirements.get(entityRequirement.id()), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInRooms(name: EntityName, isOther: Boolean, otherRequirement: String, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        roomEntities.add(EntityGenerationParams(id, GenerationRequirements.getOther(otherRequirement), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInRooms(name: EntityName, identityRequirement: Identity, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        roomEntities.add(EntityGenerationParams(id, GenerationRequirements.get(identityRequirement), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInRooms(name: EntityName, requirements: List<EntityPositionRequirement>, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        roomEntities.add(EntityGenerationParams(id, requirements, amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }


    fun addInCorridors(name: EntityName, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        corridorEntities.add(EntityGenerationParams(id, GenerationRequirements.get(id), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInCorridors(name: EntityName, entityRequirement: EntityName, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        corridorEntities.add(EntityGenerationParams(id, GenerationRequirements.get(entityRequirement.id()), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInCorridors(name: EntityName, isOther: Boolean, otherRequirement: String, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        corridorEntities.add(EntityGenerationParams(id, GenerationRequirements.getOther(otherRequirement), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInCorridors(name: EntityName, identityRequirement: Identity, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        corridorEntities.add(EntityGenerationParams(id, GenerationRequirements.get(identityRequirement), amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }

    fun addInCorridors(name: EntityName, requirements: List<EntityPositionRequirement>, amount: Float, asProbability: Boolean = false, replaceUnderneath: Boolean = false): RoomFinderAlgorithm {
        val id = Entities.getId(name)
        corridorEntities.add(EntityGenerationParams(id, requirements, amount, asProbability, replaceUnderneath))
        return thisAlgorithm
    }
}
