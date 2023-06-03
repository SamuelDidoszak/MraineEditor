package com.neutrino.util

import com.badlogic.gdx.Gdx
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.TextureAttribute

object Animations {
    private val animations = ArrayList<AnimationData>(10)

    fun add(animation: AnimationData) {
        animations.add(animation)
    }

    fun remove(animation: AnimationData) {
        animations.removeIf { it.entity == animation.entity && it.animation == animation.animation }
    }

    fun remove(entity: Entity) {
        animations.removeIf { it.entity == entity }
    }

    fun play() {
        val iterator = animations.listIterator()
        while (iterator.hasNext()) {
            val animation = iterator.next()
            val remove = !animation.animation.setFrame(Gdx.graphics.deltaTime)
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
