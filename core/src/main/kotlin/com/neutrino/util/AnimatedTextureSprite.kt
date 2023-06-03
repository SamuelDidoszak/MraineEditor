package com.neutrino.util

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.neutrino.entities.attributes.Animated

class AnimatedTextureSprite(
    textureList: Array<AtlasRegion>,
    looping: Boolean = true,
    val animationSpeed: Float = 0.16666667f
): TextureSprite(textureList[0]) {
    private val animation = Animation<AtlasRegion>(
        Animated.ANIMATION_SPEED, textureList,
        if (looping) Animation.PlayMode.LOOP else Animation.PlayMode.NORMAL
    )

    private var stateTime = 0f

    /**
     * Sets the new frame of animation.
     * @return false if animation is not looping and it finished
     */
    fun setFrame(deltaTime: Float): Boolean {
        stateTime += deltaTime
        if (animation.isAnimationFinished(stateTime))
            return false
        texture = animation.getKeyFrame(stateTime) as AtlasRegion
        return true
    }
}
