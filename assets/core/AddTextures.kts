
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.utils.Array
import com.neutrino.textures.*

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
Textures add TextureSprite(get("entities", "dungeonWall$1"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$2"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$3"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$4"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWall$5"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWallInside"), z = 0)
Textures add TextureSprite(get("entities", "dungeonWallTop$1"), 0f, 21f)
Textures add TextureSprite(get("entities", "dungeonWallTop$2"), 0f, 21f)
Textures add TextureSprite(get("entities", "dungeonWallTop$3"), 0f, 21f)
Textures add TextureSprite(get("entities", "dungeonWallTop$4"), 0f, 21f)
Textures add TextureSprite(get("entities", "dungeonWallTop$5"), 0f, 21f)
Textures add TextureSprite(get("entities", "dungeonWallTopInside"), 0f, 16f)
Textures add TextureSprite(get("entities", "dungeonWallSide$1"))
Textures add TextureSprite(get("entities", "dungeonWallSide$2"))
Textures add TextureSprite(get("entities", "dungeonWallSide$3"))
Textures add TextureSprite(get("entities", "dungeonWallSide$4"))
Textures add TextureSprite(get("entities", "dungeonWallSide$5"))
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$1"), 0f, 16f)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$2"), 0f, 16f)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$3"), 0f, 16f)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$4"), 0f, 16f)
Textures add TextureSprite(get("entities", "dungeonWallSideSmall$5"), 0f, 16f)
Textures add TextureSprite(get("entities", "woodenDoor"), 0f, 2f)
Textures add TextureSprite(get("entities", "woodenDoorClosed"))
Textures add TextureSprite(get("entities", "woodenDoorVertical"), 0f, 14f)
Textures add TextureSprite(get("entities", "woodenDoorVerticalClosed"), 13f)
Textures add TextureSprite(get("entities", "woodenDoorArched"), 0f, 2f)
Textures add TextureSprite(get("entities", "woodenDoorArchedClosed"))
Textures add TextureSprite(get("entities", "woodenDoorArchedVertical"), 0f, 14f)
Textures add TextureSprite(get("entities", "woodenDoorArchedVerticalClosed"), 13f)
Textures add TextureSprite(get("entities", "stonePillarTop"), 2f, 2f)
Textures add TextureSprite(get("entities", "candleSingle$1"), LightSources(Light(1f, 8f, Color.valueOf("e5842868"), 4f, 48f)), 10f, 8f)
Textures add TextureSprite(get("entities", "candleSingle$2"), LightSources(Light(1f, 6f, Color.valueOf("e5842868"), 4f, 48f)), 8f, 4f)
Textures add TextureSprite(get("entities", "candleSingle$3"), LightSources(Light(2f, 5f, Color.valueOf("f3642268"), 4f, 48f)), 7f, 3f)
Textures add TextureSprite(get("entities", "candleSingle$4"), LightSources(Light(2f, 8f, Color.valueOf("f3642268"), 4f, 48f)), 4f, 4f)
Textures add TextureSprite(get("entities", "candleSingle$5"), LightSources(Light(0f, 5f, Color.valueOf("dc5e2668"), 4f, 48f)), 11f, 11f)
Textures add TextureSprite(get("entities", "clayPot$1"), 1f, 3f)
Textures add TextureSprite(get("entities", "clayPot$2"), 0f, 5f)
Textures add TextureSprite(get("entities", "clayPot$3"), 4f, 4f)
Textures add TextureSprite(get("entities", "clayPot$4"), 2f, 8f)
Textures add TextureSprite(get("entities", "clayPot$1Destroyed"), 0f, 1f)
Textures add TextureSprite(get("entities", "clayPot$2Destroyed"), 1f, 2f)
Textures add TextureSprite(get("entities", "clayPot$3Destroyed"), 4f, 1f)
Textures add TextureSprite(get("entities", "clayPot$4Destroyed"), 2f, 1f)
Textures add AnimatedTextureSprite(getArray("entities", "standingTorch$1#1", "standingTorch$1#2", "standingTorch$1#3", ), true, 0.25f, LightSources(listOf(arrayListOf(
		Light(6f, 23f, Color.valueOf("f36422a2"), 12f, 512f),
	),arrayListOf(
		Light(6f, 23f, Color.valueOf("f36422a2"), 12f, 512f),
	),arrayListOf(
		Light(6f, 23f, Color.valueOf("f36422a2"), 12f, 512f),
	),)), 1f, 4f)
Textures add AnimatedTextureSprite(getArray("entities", "standingTorch$2#1", "standingTorch$2#2", "standingTorch$2#3", ), true, 0.25f, LightSources(listOf(arrayListOf(
		Light(6f, 21f, Color.valueOf("f36422a0"), 10f, 384f),
	),arrayListOf(
		Light(6f, 21f, Color.valueOf("f36422a0"), 10f, 384f),
	),arrayListOf(
		Light(6f, 21f, Color.valueOf("f36422a0"), 10f, 384f),
	),)), 1f, 4f)
Textures add AnimatedTextureSprite(getArray("entities", "torchFront#1", "torchFront#2", "torchFront#3", ), true, 0.25f, LightSources(listOf(arrayListOf(
		Light(2f, 7f, Color.valueOf("f3510783"), 6f, 128f),
	),arrayListOf(
		Light(2f, 7f, Color.valueOf("f3510783"), 6f, 128f),
	),arrayListOf(
		Light(2f, 7f, Color.valueOf("f3510783"), 6f, 128f),
	),)), 5f, 19f)
Textures add AnimatedTextureSprite(getArray("entities", "torchSide#1", "torchSide#2", "torchSide#3", ), true, 0.25f, LightSources(listOf(arrayListOf(
		Light(2f, 5f, Color.valueOf("f3510783"), 6f, 128f),
	),arrayListOf(
		Light(2f, 5f, Color.valueOf("f3510783"), 6f, 128f),
	),arrayListOf(
		Light(2f, 5f, Color.valueOf("f3510783"), 6f, 128f),
	),)), -1f, 3f)
Textures add TextureSprite(get("entities", "barrel"), 3f)
Textures add TextureSprite(get("entities", "barrelDestroyed"))
Textures add TextureSprite(get("entities", "crateBiggerDark"), 2f)
Textures add TextureSprite(get("entities", "crateBiggerDarkDestroyed"))
Textures add TextureSprite(get("entities", "crateSmall"), 3f)
Textures add TextureSprite(get("entities", "crateSmallDestroyed"))
Textures add TextureSprite(get("entities", "woodenChestMid"), 1f, 1f)
