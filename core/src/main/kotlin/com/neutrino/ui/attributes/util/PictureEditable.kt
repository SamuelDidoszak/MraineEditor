package com.neutrino.ui.attributes.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.neutrino.textures.Light
import com.neutrino.textures.LightSources
import com.neutrino.util.Pixel
import com.neutrino.util.PixelData
import com.sksamuel.scrimage.AutocropOps
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.color.Colors
import com.sksamuel.scrimage.color.RGBColor
import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.pixels.PixelsExtractor
import java.awt.Rectangle

class PictureEditable(val file: FileHandle) {

    private var image = ImmutableImage.loader().fromFile(file.file())
    private val lights: ArrayList<Light> = ArrayList()

    fun edit() {
        crop()
        saveAsTemporary()
        readLightsFromAlphaRemoveTransparency()
        saveAsTemporary()
    }

    fun crop() {
        image = image.autocrop()
    }

    fun saveAsTemporary() {
        val folder = Gdx.files.local("textureSources/temp")
        folder.mkdirs()
        image.output(PngWriter.NoCompression, getTempFile().file())
    }

    fun getEditedTexture(): Texture {
        return Texture(getTempFile())
    }

    fun getXOffset(): Int {
        return AutocropOps.scanright(Colors.Transparent.awt(), image.height, image.width, 0, pixelExtractor(), 0)
    }

    fun getYOffset(): Int {
        return image.height - AutocropOps.scanup(Colors.Transparent.awt(), image.width, image.height - 1, pixelExtractor(), 0) - 1
    }


    fun getTempFile(): FileHandle {
        return Gdx.files.local("textureSources/temp/${file.name()}")
    }

    private fun pixelExtractor(): PixelsExtractor {
        return PixelsExtractor { r: Rectangle -> image.pixels(r.x, r.y, r.width, r.height) }
    }

    /**
     * Reads lights from transparent pixels and removes transparency from the image
     */
    fun readLightsFromAlphaRemoveTransparency() {
        val texture = Texture(getTempFile())
        val pixelData = PixelData(texture)
        var pixel: Pixel

        for (y in 0 until texture.height) {
            for (x in 0 until texture.width) {
                pixel = pixelData.getPixel(x, y)
                if (pixel.a() in 100..250) {
                    lights.add(Light(x.toFloat(), (texture.height - y - 1).toFloat(),
                        Color(pixel.color().r, pixel.color().g, pixel.color().b, 1f)))
                    // Remove transparency from the image
                    image.setColor(x, y, RGBColor(
                        (pixel.color().r * 255).toInt(),
                        (pixel.color().g * 255).toInt(),
                        (pixel.color().b * 255).toInt(),
                        255))
                }
            }
        }
    }

    fun getLightSources(): LightSources? {

        if (lights.isEmpty())
            return null
        if (lights.size == 1)
            return LightSources(lights[0])
        else
            return LightSources(lights)

    }
}
