package com.neutrino.textures

import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.TextureAttribute

class Animations {
    private val animations = ArrayList<AnimationData>(10)

    fun add(animation: AnimationData) {
        animations.add(animation)
    }

    fun remove(animation: AnimationData) {
        animations.remove(animation)
    }

    fun remove(entity: Entity) {
        animations.removeIf { it.entity == entity }
    }

    fun update(deltaTime: Float) {
        play(deltaTime)
    }

    fun play(deltaTime: Float) {
        val iterator = animations.listIterator()
        while (iterator.hasNext()) {
            val animation = iterator.next()
            val remove = !animation.animation.setFrame(deltaTime)
            if (remove) {
                iterator.remove()
                if (animation.nextAnimation == null)
                    continue
                animation.entity.get(TextureAttribute::class)!!
                    .textures[animation.entity.get(TextureAttribute::class)!!.textures.indexOf(animation.animation)] = animation.nextAnimation
                iterator.add(AnimationData(animation.nextAnimation, animation.entity, null))
            }
        }
    }
}
