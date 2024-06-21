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

    fun editAnimation() {
        cropUpRight()
        saveAsTemporary()
        readLightsFromAlphaRemoveTransparency()
        saveAsTemporary()
    }

    fun crop() {
        val x1 = AutocropOps.scanright(Colors.Transparent.awt(), image.height, image.width, 0, pixelExtractor(), 0)
        val x2 = AutocropOps.scanleft(Colors.Transparent.awt(), image.height, image.width - 1, pixelExtractor(), 0)
        val y1 = AutocropOps.scandown(Colors.Transparent.awt(), image.height, image.width, 0, pixelExtractor(), 0)
        val y2 = AutocropOps.scanup(Colors.Transparent.awt(), image.width, image.height - 1, pixelExtractor(), 0)

        if (x1 == 0 && y1 == 0 && x2 == image.width - 1 && y2 == image.height - 1)
            return
        image = image.subimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1)
    }

    fun cropUpRight() {
        val x2 = AutocropOps.scanleft(Colors.Transparent.awt(), image.height, image.width - 1, pixelExtractor(), 0)
        val y1 = AutocropOps.scandown(Colors.Transparent.awt(), image.height, image.width, 0, pixelExtractor(), 0)

        if (x2 == image.width - 1 && y1 == 0)
            return
        image = image.subimage(0, y1, x2 + 1, image.height - y1)
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
