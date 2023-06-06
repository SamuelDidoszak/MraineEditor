package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.textures.LevelDrawer

class OnMapPositionAttribute(
    var x: Int,
    var y: Int,
    var level: LevelDrawer
): Attribute()
