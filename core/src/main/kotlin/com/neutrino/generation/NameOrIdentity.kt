package com.neutrino.generation

import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityId
import com.neutrino.util.EntityName
import com.neutrino.util.hasIdentity
import com.neutrino.util.id
import kotlin.reflect.KClass

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

    fun getEntityName(): String? {
        if (id == null)
            return null
        return Entities.getName(id)
    }

    fun getIdentityName(): String? {
        if (identity == null)
            return null
        return identity.simpleName
    }
}
