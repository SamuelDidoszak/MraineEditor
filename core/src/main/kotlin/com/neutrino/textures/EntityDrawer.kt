package com.neutrino.textures

import com.neutrino.entities.Entity

interface EntityDrawer {

    val animations: Animations
    val lights: ArrayList<Pair<Entity, Light>>

    var map: List<List<MutableList<Entity>>>

    fun addTexture(entity: Entity, texture: TextureSprite)
    fun removeTexture(entity: Entity, texture: TextureSprite)
}
