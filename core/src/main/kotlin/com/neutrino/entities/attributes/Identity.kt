package com.neutrino.entities.attributes

import attributes.Attribute

sealed class Identity: Attribute() {

    class Any: Identity()
    class Floor: Identity()
    class Wall: Identity()

}
