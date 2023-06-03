package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.OnMapPosition
import com.neutrino.util.AnimatedTextureSprite
import com.neutrino.util.AnimationData
import com.neutrino.util.Animations
import com.neutrino.util.TextureSprite
import kotlin.random.Random

class TextureAttribute(
    private val setTextures: (position: OnMapPosition?,
                              random: Random?,
                              textures: ArrayList<TextureSprite>) -> Unit
): Attribute() {
    val textures: ArrayList<TextureSprite> = ArrayList(1)

    fun setTextures() {
        setTextures(null, null)
    }

    fun setTextures(onMapPosition: OnMapPosition?, randomGenerator: Random?) {
        setTextures.invoke(onMapPosition, randomGenerator, textures)

        for (texture in textures) {
            if (texture is AnimatedTextureSprite)
                Animations.add(AnimationData(texture, entity))
        }
    }

    /**
     * Sets the animation for textures[0]
     */
    fun setAnimation(animation: AnimatedTextureSprite, nextAnimation: AnimatedTextureSprite?) {
        textures[0] = animation
        Animations.add(AnimationData(animation, entity, nextAnimation))
    }

    fun finalize() {
        for (texture in textures) {
            if (texture is AnimatedTextureSprite) {
                Animations.remove(entity)
                return
            }
        }
    }
}
