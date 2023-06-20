
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.neutrino.textures.Textures

fun get(atlas: String, region: String): AtlasRegion {
    return Textures.atlases[atlas]!!.findRegion(region)
}

fun getArray(atlas: String, vararg regions: String): Array<AtlasRegion> {
    val textureArray: Array<AtlasRegion> = Array()
    for (region in regions) {
        textureArray.add(Textures.atlases[atlas]!!.findRegion(region))
    }
    return textureArray
}

for (atlas in Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures").list(".atlas")) {
    Textures.atlases[atlas.nameWithoutExtension()] = TextureAtlas(atlas)
}

