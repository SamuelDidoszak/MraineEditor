package com.neutrino.entities.util

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.MapParamsAttribute
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.textures.Textures

sealed class Interaction(var requiredDistance: Int, var isPrimary: Boolean, var actionIcon: String, var turnCost: Double = 0.0) {

    open fun act() {}

    open class DOOR(): Interaction(1, true,"", turnCost = 1.0), RequiresEntityParameter {

        override lateinit var entity: Entity
        var open = false
        override fun act() {
            open = !open
            entity.get(MapParamsAttribute::class)!!.allowOnTop = open
            entity.get(MapParamsAttribute::class)!!.allowCharacterOnTop = open
            // TODO check textures
            val textureName = if (open) entity.get(TextureAttribute::class)!!.textures[0].texture.name.substringBefore("Closed")
                else entity.get(TextureAttribute::class)!!.textures[0].texture.name.plus("Closed")
            entity.get(TextureAttribute::class)!!.textures[0] = Textures.get(textureName)
            this.isPrimary = !open
        }
    }

    class ITEM: Interaction(0, true, "", turnCost = 1.0)

    class DESTROY(): Interaction(1, true, "", 1.0), RequiresEntityParameter {
        override lateinit var entity: Entity
    }

    class OPEN: Interaction(1, true, "", 1.0)
}
