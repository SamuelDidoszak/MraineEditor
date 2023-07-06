package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.Interaction

class DestructableAttribute(var entityHp: Float): Attribute() {
    var destroyed: Boolean = false

    init {
        if (!(entity has InteractionAttribute::class))
            entity addAttribute InteractionAttribute(arrayListOf())
        val interaction = Interaction.DESTROY()
        interaction.entity = entity
        entity.get(InteractionAttribute::class)!!.interactionList.add(interaction)
    }

//    fun getDamage(data: AttackData, coord: Coord) {
//        entityHp -= data.getDamageSum()
//        if (entityHp.lessThanDelta(0f)) {
//            destroy(coord)
//        }
//    }
//
//    fun destroy(coord: Coord) {
//        val items = destroy()
//        if (items != null) {
//            for (item in items) {
//                LevelArrays.getEntitiesAt(coord).add(ItemEntity(item))
//            }
//        }
//        LevelArrays.getImpassableList().remove(coord)
//    }
//
//    fun destroy(): MutableList<Item>? {
//        destroyed = true
//        if (this is Entity) {
//            allowOnTop = true
//            allowCharacterOnTop = true
//            texture = getTexture(texture.name + "Destroyed")
//        }
//        if (this is Container) {
//            return dropItems()
//        }
//        return null
//    }
}
