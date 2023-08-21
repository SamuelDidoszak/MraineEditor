package com.neutrino.textures

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.OnMapPositionAttribute
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.util.Constants.SCALE
import com.neutrino.util.Optimize
import java.util.*
import kotlin.math.min
import kotlin.random.Random

class SingleEntityDrawer(entity: Entity): Actor(), EntityDrawer {

    override val animations: Animations = Animations()
    override val lights: ArrayList<Pair<Entity, Light>> = ArrayList()
    @Optimize
    private val textureLayers: SortedMap<Int, LayeredTextureList> = sortedMapOf()

    var centered = true
    private var scale = SCALE
    private var offsetX = 0f
    private var offsetY = 0f

    override var map: List<List<MutableList<Entity>>> = getEmptyEntityList()
    private var entity: Entity = entity
        set(value) {
            if (textureLayers.isNotEmpty())
                textureLayers.clear()
            field = value
            map[1][1][0] = field
            field.addAttribute(OnMapPositionAttribute(1, 1, this))
            val textureAttribute = field.get(TextureAttribute::class) ?:
                field.addAttribute(TextureAttribute {_, _, _ ->}).get(TextureAttribute::class)!!
            textureAttribute.setTextures(null, Random)
            if (textureAttribute.textures.isEmpty()) {
                println(entity.name + " has no texture set!")
                textureAttribute.textures.add(Textures.get("backgroundTexture"))
            }
            updateScale()
        }

    init {
        this.entity = entity
    }

    override fun addTexture(entity: Entity, texture: TextureSprite) {
        if (textureLayers[texture.z] == null)
            textureLayers[texture.z] = LayeredTextureList()
        textureLayers[texture.z]!!.add(LayeredTexture(entity, texture))
    }

    override fun removeTexture(entity: Entity, texture: TextureSprite) {
        textureLayers[texture.z]!!.removeIf { it.entity == entity && it.texture == texture }
    }

    private fun getEmptyEntityList(): List<List<MutableList<Entity>>> {
        val list = arrayListOf<ArrayList<MutableList<Entity>>>()
        for (y in 0 until 9) {
            list.add(arrayListOf())
            for (x in 0 until 9) {
                list[y].add(mutableListOf())
                val entity = Entity()
                entity.id = -1
                list[y][x].add(entity)
            }
        }
        return list
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val textures = entity.get(TextureAttribute::class)!!.textures
        for (texture in textures) {
            if (texture.z == 0)
                batch!!.draw(texture.texture,
                    if (!texture.mirrorX) x + texture.x * scale + offsetX
                        else x + texture.x * scale + offsetX + texture.texture.regionWidth * scale,
                    y + texture.y * scale + offsetY,
                    texture.texture.regionWidth * if (!texture.mirrorX) scale else -1 * scale,
                    texture.texture.regionHeight * scale)
        }
        for (layer in textureLayers) {
            for (layeredTexture in layer.value) {
                val texture = layeredTexture.texture
                batch!!.draw(texture.texture,
                    if (!texture.mirrorX) x + texture.x * scale + offsetX
                        else x + texture.x * scale + offsetX + layeredTexture.texture.width() * scale,
                    y + texture.y * scale + offsetY,
                    layeredTexture.texture.width() * if (!texture.mirrorX) scale else -1 * scale,
                    layeredTexture.texture.height() * scale)
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        animations.play(delta)
    }

    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        updateScale()
    }

    private fun updateScale() {
        val textures = entity.get(TextureAttribute::class)!!.textures
        // maybe add abs(it.x)
        val maxWidth = textures.maxOf { it.width() + it.x * if(centered) 2 else 1 }
        val maxHeight = textures.maxOf { it.height() + it.y }

        val wScale: Float = width / maxWidth
        val hScale: Float = height / maxHeight
        scale = min(wScale, hScale)

        if (!centered) {
            offsetX = 0f
            offsetY = 0f
            return
        }

        offsetX = (width - maxWidth * scale) / 2
        offsetY = (height - maxHeight * scale) / 2
    }
}
