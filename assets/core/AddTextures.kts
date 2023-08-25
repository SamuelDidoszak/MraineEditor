
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.neutrino.textures.Light
import com.neutrino.textures.LightSources
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

for (atlas in Gdx.files.absolute("${Gdx.files.localStoragePath}/assets/textures").list(".atlas")) {
    Textures.atlases[atlas.nameWithoutExtension()] = TextureAtlas(atlas)
}

Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$1"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$2"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$3"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$4"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$5"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$6"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$7"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$8"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$9"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$10"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$11"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$12"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$13"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$14"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$15"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$16"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$17"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorBasic$18"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorCracked$1"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorCracked$2"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorCracked$3"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorCracked$4"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$1"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$2"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$3"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$4"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$5"), z = 0)
Textures add TextureSprite(get("entities", "cleanDungeonFloorSmaller$6"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$5"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$6"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$7"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$8"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$9"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$10"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$11"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$12"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$13"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$14"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$15"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$16"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$17"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorBasic$18"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorCracked$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorCracked$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorCracked$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorCracked$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorLights$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorLights$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorLights$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorLights$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorLights$5"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$5"), z = 0)
Textures add TextureSprite(get("entities", "dungeonFloorSmaller$6"), z = 0)
Textures add TextureSprite(get("entities", "dungeonStairsDown"), z = 0)
Textures add TextureSprite(get("entities", "dungeonStairsUp"), z = 0)
Textures add TextureSprite(get("entities", "stonePillar"), 2f, 10f)
Textures add TextureSprite(get("entities", "stonePillarCracked"), 2f, 10f)
Textures add TextureSprite(get("entities", "woodenDoorClosed"))
Textures add TextureSprite(get("entities", "candlesMultiple$3"), LightSources(arrayListOf(
	Light(10f, 2f, Color.valueOf("f3642268"), 4f, 48f),
	Light(3f, 5f, Color.valueOf("f3642268"), 4f, 48f),
	Light(10f, 10f, Color.valueOf("dc5e2666"), 2f, 24f),
),))
Textures add TextureSprite(get("entities", "dungeonWall$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$5"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWallInside"), z = 10)
Textures add TextureSprite(get("entities", "dungeonWallTop$1"), 0f, 21f, 11)
Textures add TextureSprite(get("entities", "dungeonWallTop$2"), 0f, 21f, 11)
Textures add TextureSprite(get("entities", "dungeonWallTop$3"), 0f, 21f, 11)
Textures add TextureSprite(get("entities", "dungeonWallTop$4"), 0f, 21f, 11)
Textures add TextureSprite(get("entities", "dungeonWallTop$5"), 0f, 21f, 11)
Textures add TextureSprite(get("entities", "dungeonWallTopInside"), 0f, 16f, 10)
Textures add TextureSprite(get("entities", "dungeonWallSide$1"), z = 11)
Textures add TextureSprite(get("entities", "dungeonWallSide$2"), z = 11)
Textures add TextureSprite(get("entities", "dungeonWallSide$3"), z = 11)
Textures add TextureSprite(get("entities", "dungeonWallSide$4"), z = 11)
Textures add TextureSprite(get("entities", "dungeonWallSide$5"), z = 11)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$1"), 0f, 16f, 11)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$2"), 0f, 16f, 11)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$3"), 0f, 16f, 11)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$4"), 0f, 16f, 11)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$5"), 0f, 16f, 11)
Textures add TextureSprite(get("entities", "barrel"))
