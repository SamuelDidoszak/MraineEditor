package com.neutrino.entities.util

import com.neutrino.entities.Entity

data class OnMapPosition(
    val map: List<List<MutableList<Entity>>>,
    val xPos: Int,
    val yPos: Int,
    val zPos: Int
)
