package com.neutrino.textures

import com.badlogic.gdx.graphics.Color

data class Light(
    val x: Float,
    val y: Float,
    val color: Color,
    val intensity: Float = color.a * 255f % 25f,
    val radius: Float = setRadius(intensity)
) {

    override fun toString(): String {
        return "Light($x, $y, Color($color), ${"%.3f".format(intensity)}, ${"%.3f".format(radius)})"
    }

    companion object {
        private fun setRadius(intensity: Float): Float {
            return when (intensity.toInt()) {
                0 -> 8f
                1 -> 16f
                2 -> 24f
                3 -> 32f
                4 -> 48f
                5 -> 64f
                6 -> 128f
                7 -> 192f
                8 -> 256f
                9 -> 320f
                10 -> 384f
                11 -> 448f
                12 -> 512f
                13 -> 640f
                14 -> 768f
                15 -> 896f
                16 -> 1024f
                17 -> 1280f
                18 -> 1536f
                19 -> 1792f
                20 -> 2048f
                21 -> 2560f
                22 -> 3072f
                23 -> 3584f
                24 -> 4096f
                else -> 32f
            }
        }
    }
}
