package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.OnMapPosition
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.AnimationData
import com.neutrino.textures.TextureSprite
import kotlin.random.Random

class TextureAttribute(
    private val setTextures: (position: OnMapPosition?,
                              random: Random,
                              textures: ArrayList<TextureSprite>) -> Unit
): Attribute() {
    val textures: TextureList = TextureList()

    fun setTextures(onMapPosition: OnMapPosition?, randomGenerator: Random) {
        setTextures.invoke(onMapPosition, randomGenerator, textures)
    }

    /**
     * Sets the animation for textures[0]
     */
    fun setAnimation(animation: AnimatedTextureSprite, nextAnimation: AnimatedTextureSprite?, index: Int = 0) {
        textures.set(index, animation, AnimationData(animation, entity, nextAnimation))
    }

    fun finalize() {
        textures.removeIf { true }
    }

    inner class TextureList: ArrayList<TextureSprite>(1) {

        override fun set(index: Int, element: TextureSprite): TextureSprite {
            val oldElement = super.set(index, element)
            removeFromLevel(oldElement)
            addToLevel(element)
            return oldElement
        }

        fun set(index: Int, element: AnimatedTextureSprite, animationData: AnimationData): TextureSprite {
            val oldElement = super.set(index, element)
            removeFromLevel(oldElement)
            addToLevel(element, animationData)
            return oldElement
        }

        override fun add(element: TextureSprite): Boolean {
            addToLevel(element)
            return super.add(element)
        }

        override fun remove(element: TextureSprite): Boolean {
            removeFromLevel(element)
            return super.remove(element)
        }

        override fun removeAt(index: Int): TextureSprite {
            val element = super.removeAt(index)
            removeFromLevel(element)
            return element
        }

        private fun addToLevel(element: TextureSprite, animationData: AnimationData? = null) {
            val level = entity.get(OnMapPositionAttribute::class)!!.level
            if (element.z != 0)
                level.addTexture(entity, element)
            if (element is AnimatedTextureSprite)
                level.animations.add(animationData ?: AnimationData(element, entity))
            if (element.lights != null) {
                if (element.lights!!.isSingleLight)
                    level.lights.add(Pair(entity, element.lights!!.getLight()))
                else
                    for (light in element.lights!!.getLights()!!) {
                        level.lights.add(Pair(entity, light))
                    }
            }
        }

        private fun removeFromLevel(element: TextureSprite) {
            val level = entity.get(OnMapPositionAttribute::class)!!.level
            if (element.z != 0)
                level.removeTexture(entity, element)
            if (element is AnimatedTextureSprite)
                level.animations.remove(AnimationData(element, entity))
            if (element.lights != null) {
                if (element.lights!!.isSingleLight)
                    level.lights.remove(Pair(entity, element.lights!!.getLight()))
                else
                    for (light in element.lights!!.getLights()!!) {
                        level.lights.remove(Pair(entity, light))
                    }
            }
        }
    }
}
