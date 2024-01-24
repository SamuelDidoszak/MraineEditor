package com.neutrino.entities.util

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Texture
import com.neutrino.entities.attributes.map.MapParams
import com.neutrino.textures.Textures

sealed class InteractionType(var requiredDistance: Int, var isPrimary: Boolean, var actionIcon: String, var turnCost: Double = 0.0) {

    open fun act() {}

    open class DOOR(): InteractionType(1, true,"", turnCost = 1.0), RequiresEntityParameter {

        override lateinit var entity: Entity
        var open = false
        override fun act() {
            open = !open
            entity.get(MapParams::class)!!.allowOnTop = open
            entity.get(MapParams::class)!!.allowCharacterOnTop = open
            // TODO check textures
            val textureName = if (open) entity.get(Texture::class)!!.textures[0].texture.name.substringBefore("Closed")
                else entity.get(Texture::class)!!.textures[0].texture.name.plus("Closed")
            entity.get(Texture::class)!!.textures[0] = Textures.get(textureName)
            this.isPrimary = !open
        }
    }

    class ITEM: InteractionType(0, true, "", turnCost = 1.0)

    class DESTROY(): InteractionType(1, true, "", 1.0), RequiresEntityParameter {
        override lateinit var entity: Entity
    }

    class OPEN: InteractionType(1, true, "", 1.0)
}
