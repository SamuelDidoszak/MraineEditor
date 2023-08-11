
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.neutrino.textures.AnimatedTextureSprite
import com.neutrino.textures.TextureSprite
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

for (atlas in Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/editorTextures").list(".atlas")) {
    Textures.atlases[atlas.nameWithoutExtension()] = TextureAtlas(atlas)
}

Textures.add(TextureSprite(get("animationButton", "animationButton")))
Textures.add(AnimatedTextureSprite(getArray("animationButton",
    "animationButtonLooping#1", "animationButtonLooping#2",
    "animationButtonLooping#3", "animationButtonLooping#4"), animationSpeed = 0.2f))
Textures.add(AnimatedTextureSprite(getArray("animationButton",
    "animationButtonForward#1", "animationButtonForward#2",
    "animationButtonForward#3", "animationButtonForward#4",
    "animationButtonForward#5", "animationButtonForward#6",
    "animationButtonForward#7", "animationButtonForward#8",
    "animationButtonForward#9", "animationButtonForward#10",
    "animationButtonForward#11", "animationButtonForward#12", ), animationSpeed = 0.125f))
Textures add TextureSprite(get("icons", "addTexture"))
Textures add TextureSprite(get("icons", "chainTexture"))
Textures add TextureSprite(get("icons", "stopTexture"))
Textures add TextureSprite(get("icons", "chainStopTexture"))
Textures add TextureSprite(get("icons", "backgroundTexture"))
Textures add TextureSprite(get("icons", "addButtonTexture"))
Textures add TextureSprite(get("icons", "rulesTexture"))
Textures add TextureSprite(get("icons", "notTexture"))
Textures add TextureSprite(get("icons", "notActiveTexture"))
Textures add TextureSprite(get("identities", "wallIdentity"))
Textures add TextureSprite(get("identities", "floorIdentity"))
Textures add TextureSprite(get("identities", "anyIdentity"))













