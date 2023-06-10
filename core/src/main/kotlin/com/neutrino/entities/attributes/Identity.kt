package com.neutrino.entities.attributes

import attributes.Attribute

sealed class Identity: Attribute() {

    class Floor: Identity()
    class Wall: Identity()

}
