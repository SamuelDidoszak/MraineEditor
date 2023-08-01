package com.neutrino.util

import com.neutrino.entities.Entities

typealias EntityId = Int
typealias EntityName = String

@JvmInline
value class EntityNameId(val id: EntityId) {
    constructor(name: EntityName) : this(Entities.getId(name))
}
