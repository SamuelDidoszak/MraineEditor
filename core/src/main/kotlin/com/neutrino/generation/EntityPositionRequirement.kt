package com.neutrino.generation

import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityId
import com.neutrino.util.EntityName
import com.neutrino.util.hasIdentity
import com.neutrino.util.id
import kotlin.reflect.KClass

class EntityPositionRequirement {
    var requirementType: EntityPositionRequirementType
    private var requiredEntity: NameOrIdentity? = null
    var requirementList: List<Pair<Int, NameOrIdentity>>

    /** Use this constructor to interpret next values as grouped with requirementType group type */
    constructor(requirementType: EntityPositionRequirementType) {
        this.requirementType = requirementType
        this.requirementList = listOf()
    }

    constructor(requirementType: EntityPositionRequirementType,
                requiredEntity: NameOrIdentity, list: List<Int>) {
        this.requirementType = requirementType
        this.requiredEntity = requiredEntity
        requirementList = list.map { Pair(it, requiredEntity) }
    }

    constructor(requirementType: EntityPositionRequirementType, list: List<Pair<Int, NameOrIdentity>>) {
        this.requirementType = requirementType
        requirementList = list
    }

    constructor(requirementType: EntityPositionRequirementType,
                requiredEntity: EntityName, list: List<Int>) {
        this.requirementType = requirementType
        this.requiredEntity = NameOrIdentity(requiredEntity)
        requirementList = list.map { Pair(it, this.requiredEntity!!) }
    }

    constructor(requirementType: EntityPositionRequirementType,
                requiredEntity: KClass<out Identity>, list: List<Int>) {
        this.requirementType = requirementType
        this.requiredEntity = NameOrIdentity(requiredEntity)
        requirementList = list.map { Pair(it, this.requiredEntity!!) }
    }
}

class NameOrIdentity(name: EntityName? = null, val identity: KClass<out Identity>? = null) {
    constructor(identity: KClass<out Identity>) : this(null, identity)

    val id = name?.id()

    fun isSame(entity: Entity): Boolean {
        return entity.id == id || (identity != null && entity hasIdentity identity)
    }

    fun isSame(name: EntityName): Boolean {
        return id != null && Entities.getName(id) == name
    }

    fun isSame(id: EntityId): Boolean {
        return this.id == id
    }
}

enum class EntityPositionRequirementType {
    /** All of the requirements have to be fulfilled for generation.
     * In a group: all of below requirements have to be fulfilled*/
    AND,
    /** Passed requirements cannot be fulfilled.
     * In a group: will not generate if below requirements were fulfilled*/
    NAND,
    /** Any of the requirements have to be fulfilled for generation.
     * In a group: any of below requirements have to be fulfilled*/
    OR,
    /** None of the requirements can be fulfilled.
     * In a group: none of below requirements can be fulfilled*/
    NOR,
}
