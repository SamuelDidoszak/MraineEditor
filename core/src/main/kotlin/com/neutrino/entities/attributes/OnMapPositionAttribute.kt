package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.Entity
import com.neutrino.textures.EntityDrawer

class OnMapPositionAttribute(
    var x: Int,
    var y: Int,
    var level: EntityDrawer
): Attribute() {
    fun getMap(): List<List<MutableList<Entity>>> {
        return level.map
    }
}
