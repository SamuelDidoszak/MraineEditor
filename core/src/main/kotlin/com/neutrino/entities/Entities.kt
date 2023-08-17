package com.neutrino.entities


object Entities {
    private val entityIds: HashMap<String, Int> = HashMap()
    private val entityNames: ArrayList<String> = ArrayList()
    private val entityFactory: MutableList<() -> Entity> = mutableListOf()

    /**
     * Adds or replaces the entity. Method available only for entity editing
     */
    fun add(name: String, entity: () -> Entity) {
        val entityExists = entityIds[name] != null
        if (entityExists) {
            entityFactory[entityIds[name]!!] = entity
        } else {
            entityIds[name] = entityFactory.size
            entityNames.add(name)
            entityFactory.add(entity)
        }
    }

//    fun add(name: String, entity: () -> Entity) {
//        entityIds[name] = entityFactory.size
//        entityNames.add(name)
//        entityFactory.add(entity)
//    }

    fun new(name: String): Entity {
        try {
            return new(entityIds[name]!!)
        } catch (_: Exception) {
            println("Entity with name: $name does not exist!")
        }
        throw Exception()
    }

    fun new(id: Int): Entity {
        try {
            val entity = entityFactory[id].invoke()
            entity.id = id
            return entity
        } catch (_: Exception) {
            println("Entity with id: $id ${if (id < entityNames.size) "name: ${entityNames[id]} " else ""}does not exist!")
        }
        throw Exception()
    }

    fun getId(name: String): Int {
        try {
            return entityIds[name]!!
        } catch (_: Exception) {
            Exception("Entity with name: $name does not exist!").toString()
        }
        throw Exception()
    }

    fun getName(id: Int): String {
        try {
            return entityNames[id]
        } catch (_: Exception) {
            println("Entity with id: $id ${if (id < entityNames.size) "name: ${entityNames[id]} " else ""}does not exist!")
        }
        throw Exception()
    }
}
