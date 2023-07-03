package com.neutrino.builders

import com.badlogic.gdx.files.FileHandle
import ktx.script.KotlinScriptEngine

class TextureBuilder {
    val scriptEngine = KotlinScriptEngine()

    private val texturesFile = FileHandle("core/AddTextures.kts")
    private val stringBuilder = StringBuilder(300)

    fun build(name: String, atlasName: String, x: Float = 0f, y: Float = 0f, z: Int = 1) {
        stringBuilder.setLength(0)
        stringBuilder.append("Textures add TextureSprite(" +
            "Textures.atlases[\"$atlasName\"]!!.findRegion(\"$name\")")
        addXYZ(x, y, z)
        stringBuilder.append(")")

        addToFile(stringBuilder.toString())
    }

    fun buildAnimation(names: List<String>, atlasName: String, looping: Boolean, speed: Float,
                       x: Float = 0f, y: Float = 0f, z: Int = 1) {
        stringBuilder.setLength(0)
        stringBuilder.append("Textures add AnimatedTextureSprite(getArray(\"$atlasName\", ")
        stringBuilder.append(names.map { it -> "\"$it\", " })
        stringBuilder.append("), $looping, $speed")
        addXYZ(x, y, z)
        stringBuilder.append(")")

        addToFile(stringBuilder.toString())
    }

    private fun addXYZ(x: Float, y: Float, z: Int) {
        if (x != 0f || y != 0f || z != 1)
            stringBuilder.append(", $x")
        if (y != 0f || z != 1)
            stringBuilder.append(", $y")
        if (z != 1)
            stringBuilder.append(", $z")
    }

    private fun addToFile(string: String) {
        texturesFile.writeString(string, true)
    }
}
