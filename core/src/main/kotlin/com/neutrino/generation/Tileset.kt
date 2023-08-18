package com.neutrino.generation

import com.neutrino.entities.attributes.Identity
import com.neutrino.util.EntityName
import com.neutrino.util.addInitial
import kotlin.random.Random

class Tileset() {

    private val entityIdentityMap: MutableMap<Identity, ArrayList<EntityName>> = mutableMapOf()

    fun getEntity(identity: Identity): EntityName? {
        return entityIdentityMap[identity]?.first()
    }

    fun getEntities(identity: Identity): List<EntityName>? {
        return entityIdentityMap[identity]
    }

    fun getRandomEntity(identity: Identity, rng: Random): EntityName? {
        val entities = entityIdentityMap[identity] ?: return null
        if (entities.size == 1)
            return entities.first()

        val randVal = rng.nextFloat() * 100f
        return entities[(randVal / entities.size).toInt()]
    }

    fun getAll(): Map<Identity, ArrayList<EntityName>> {
        return entityIdentityMap
    }

    fun add(entityIdentity: Pair<Identity, EntityName>): Tileset {
        if (entityIdentityMap[entityIdentity.first] == null)
            entityIdentityMap[entityIdentity.first] = ArrayList<EntityName>()
        entityIdentityMap[entityIdentity.first]!!.add(entityIdentity.second)
        return this
    }

    fun add(identity: Identity, entities: ArrayList<EntityName>): Tileset {
        if (entityIdentityMap[identity] == null)
            entityIdentityMap[identity] = ArrayList<EntityName>()
        entityIdentityMap[identity]!!.addAll(entities)
        return this
    }

    fun add(entityIdentities: List<Pair<Identity, EntityName>>): Tileset {
        entityIdentities.forEach {
            if (entityIdentityMap[it.first] == null)
                entityIdentityMap[it.first] = ArrayList<EntityName>()
            entityIdentityMap[it.first]!!.add(it.second)
        }
        return this
    }

    fun add(tileset: Tileset): Tileset {
        tileset.getAll().toList().forEach {
            if (entityIdentityMap[it.first] == null)
                entityIdentityMap[it.first] = ArrayList<EntityName>()
            entityIdentityMap[it.first]!!.addAll(it.second)
        }
        return this
    }

    operator fun plusAssign(tileset: Tileset) {
        add(tileset)
    }

    constructor(entityIdentity: Pair<Identity, EntityName>): this() {
        entityIdentityMap[entityIdentity.first] = ArrayList<EntityName>().addInitial(entityIdentity.second)
    }

    constructor(identity: Identity, entities: ArrayList<EntityName>): this() {
        entityIdentityMap[identity] = entities
    }

    constructor(entityIdentities: List<Pair<Identity, EntityName>>): this() {
        entityIdentities.forEach {
            entityIdentityMap[it.first] = ArrayList<EntityName>().addInitial(it.second)
        }
    }
}
