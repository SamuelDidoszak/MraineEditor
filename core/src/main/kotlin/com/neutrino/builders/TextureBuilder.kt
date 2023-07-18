package com.neutrino.builders

import com.badlogic.gdx.Gdx
import com.neutrino.textures.LightSources

class TextureBuilder {

    private val texturesFile = Gdx.files.local("assets/core/AddTextures.kts")
    private val stringBuilder = StringBuilder(300)

    fun build(name: String, atlasName: String, lightSources: LightSources?, x: Float = 0f, y: Float = 0f, z: Int = 1) {
        stringBuilder.setLength(0)
        stringBuilder.append("Textures add TextureSprite(get(\"$atlasName\", \"$name\")")
        if (lightSources != null)
            stringBuilder.append(", $lightSources")
        addXYZ(x, y, z)
        stringBuilder.append(")\n")

        addToFile(stringBuilder.toString())
    }

    fun buildAnimation(names: List<String>, atlasName: String, looping: Boolean, speed: Float, lightSources: LightSources?,
                       x: Float = 0f, y: Float = 0f, z: Int = 1) {
        stringBuilder.setLength(0)
        stringBuilder.append("Textures add AnimatedTextureSprite(getArray(\"$atlasName\", ")
        for (name in names) {
            stringBuilder.append("\"$name\", ")
        }
        stringBuilder.append("), $looping, $speed")
        if (lightSources != null)
            stringBuilder.append(", $lightSources")
        addXYZ(x, y, z)
        stringBuilder.append(")\n")

        addToFile(stringBuilder.toString())
    }

    private fun addXYZ(x: Float, y: Float, z: Int) {
        if (x == 0f && y == 0f && z != 1) {
            stringBuilder.append(", z = $z")
            return
        }
        if (x != 0f || y != 0f || z != 1)
            stringBuilder.append(", ${x.toInt()}f")
        if (y != 0f || z != 1)
            stringBuilder.append(", ${y.toInt()}f")
        if (z != 1)
            stringBuilder.append(", $z")
    }

    private fun addToFile(string: String) {
        texturesFile.writeString(string, true)
    }
}
