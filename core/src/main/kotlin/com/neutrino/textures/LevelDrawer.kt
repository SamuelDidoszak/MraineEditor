package com.neutrino.textures

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.OnMapPositionAttribute
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.util.Constants.SCALE
import com.neutrino.util.Optimize
import java.util.*

open class LevelDrawer: EntityDrawer, Group() {

    override val animations: Animations = Animations()
    override val lights: ArrayList<Pair<Entity, Light>> = ArrayList()
    private val textureLayers: SortedMap<Int, LayeredTextureList> = sortedMapOf()

    override fun addTexture(entity: Entity, texture: TextureSprite) {
        if (textureLayers[texture.z] == null)
            textureLayers[texture.z] = LayeredTextureList()
        textureLayers[texture.z]!!.add(LayeredTexture(entity, texture))
    }

    override fun removeTexture(entity: Entity, texture: TextureSprite) {
        textureLayers[texture.z]!!.removeIf { it.entity == entity && it.texture == texture }
    }

    override lateinit var map: List<List<MutableList<Entity>>>

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val gameCamera = parent.stage.camera as OrthographicCamera

        var yTop = MathUtils.floor((height - (gameCamera.position.y + gameCamera.viewportHeight * gameCamera.zoom / 2f)) / 48) + 1
        var yBottom = MathUtils.ceil((height - (gameCamera.position.y - gameCamera.viewportHeight * gameCamera.zoom / 2f)) / 48) + 2
        var xLeft: Int = MathUtils.floor((gameCamera.position.x - gameCamera.viewportWidth * gameCamera.zoom / 2f) / 48)
        var xRight = MathUtils.ceil((gameCamera.position.x + gameCamera.viewportWidth * gameCamera.zoom / 2f) / 48)

        // Make sure that values are in range
        yTop = if (yTop <= 0) 0 else if (yTop > map.size) map.size else yTop
        yBottom = if (yBottom <= 0) 0 else if (yBottom > map.size) map.size else yBottom
        xLeft = if (xLeft <= 0) 0 else if (xLeft > map[0].size) map[0].size else xLeft
        xRight = if (xRight <= 0) 0 else if (xRight > map[0].size) map[0].size else xRight

        var screenX = xLeft * 48f
        var screenY = height - (yTop * 48f)

        @Optimize // Draw textures into framebuffer once.
        for (y in yTop until yBottom) {
            for (x in xLeft until xRight) {
                for (entity in map[y][x]) {
                    val textures = entity.get(TextureAttribute::class)!!.textures
                    for (texture in textures) {
                        if (texture.z == 0)
                            batch!!.draw(
                                texture.texture, if (!texture.mirrored) screenX else screenX + texture.texture.regionWidth * SCALE,
                                screenY,
                                texture.texture.regionWidth * if (!texture.mirrored) SCALE else -1 * SCALE,
                                texture.texture.regionHeight * SCALE)
                    }
                }
                screenX += 48
            }
            screenY -= 48
            screenX = xLeft * 48f
        }

        var textureX = 0f
        var textureY = 0f
        var textureWidth = 0
        var texture: TextureSprite
        textureLayers.forEach { key, layer ->
            layer.sort()
            for (layeredTexture in layer) {
                textureX = layeredTexture.getX()
                textureY = layeredTexture.getY()
                textureWidth = layeredTexture.getWidth()
                if (textureY <= yTop && textureY + layeredTexture.getHeight() >= yBottom &&
                    textureX + textureWidth >= xLeft && textureX <= xRight) {

                    texture = layeredTexture.texture
                    batch!!.draw(texture.texture,
                        if (!texture.mirrored) x + textureX else x + textureWidth + textureX,
                        y + textureY,
                        textureWidth * if (!texture.mirrored) 1f else -1f,
                        layeredTexture.getHeight() * 1f)
                }
            }
        }
    }

    fun initializeTextures() {
        for (y in map.indices) {
            for (x in map[0].indices) {
                for (entity in map[y][x]) {
                    entity addAttribute OnMapPositionAttribute(x, y, this)
                }
            }
        }
    }

    fun initializeMap(): List<List<MutableList<Entity>>> {
        return List(100) {
            List(100) {
                ArrayList<Entity>()
            }
        }
    }
}
