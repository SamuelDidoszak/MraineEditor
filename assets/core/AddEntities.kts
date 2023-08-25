
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.*
import com.neutrino.entities.util.Interaction
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.Textures
import com.neutrino.util.add

Entities.add("DungeonFloorClean") {
	Entity()
		.addAttribute(Identity.Floor())
		.addAttribute(TextureAttribute { position, random, textures -> run {
			textures add Textures.getRandomTexture(random, listOf(
                70f to listOf("cleanDungeonFloorBasic$1", "cleanDungeonFloorBasic$2", "cleanDungeonFloorBasic$3", "cleanDungeonFloorBasic$4", "cleanDungeonFloorBasic$5", "cleanDungeonFloorBasic$6", "cleanDungeonFloorBasic$7", "cleanDungeonFloorBasic$8", "cleanDungeonFloorBasic$9", "cleanDungeonFloorBasic$10", "cleanDungeonFloorBasic$11", "cleanDungeonFloorBasic$12", "cleanDungeonFloorBasic$13", "cleanDungeonFloorBasic$14", "cleanDungeonFloorBasic$15", "cleanDungeonFloorBasic$16", "cleanDungeonFloorBasic$17", "cleanDungeonFloorBasic$18"),
                15f to listOf("cleanDungeonFloorCracked$1", "cleanDungeonFloorCracked$2", "cleanDungeonFloorCracked$3", "cleanDungeonFloorCracked$4"),
                15f to listOf("cleanDungeonFloorSmaller$1", "cleanDungeonFloorSmaller$2", "cleanDungeonFloorSmaller$3", "cleanDungeonFloorSmaller$4", "cleanDungeonFloorSmaller$5", "cleanDungeonFloorSmaller$6"),
		))}})
        .addAttribute(MapParamsAttribute(true, true))
}
Entities.add("DungeonFloor") {
	Entity()
		.addAttribute(Identity.Floor())
		.addAttribute(TextureAttribute { position, random, textures -> run {
            textures add Textures.getRandomTexture(random, listOf(
				70f to listOf("dungeonFloorBasic$1", "dungeonFloorBasic$2", "dungeonFloorBasic$3", "dungeonFloorBasic$4", "dungeonFloorBasic$5", "dungeonFloorBasic$6", "dungeonFloorBasic$7", "dungeonFloorBasic$8", "dungeonFloorBasic$9", "dungeonFloorBasic$10", "dungeonFloorBasic$11", "dungeonFloorBasic$12", "dungeonFloorBasic$13", "dungeonFloorBasic$14", "dungeonFloorBasic$15", "dungeonFloorBasic$16", "dungeonFloorBasic$17", "dungeonFloorBasic$18"),
				10f to listOf("dungeonFloorCracked$1", "dungeonFloorCracked$2", "dungeonFloorCracked$3", "dungeonFloorCracked$4"),
				10f to listOf("dungeonFloorLights$1", "dungeonFloorLights$2", "dungeonFloorLights$3", "dungeonFloorLights$4", "dungeonFloorLights$5"),
				10f to listOf("dungeonFloorSmaller$1", "dungeonFloorSmaller$2", "dungeonFloorSmaller$3", "dungeonFloorSmaller$4", "dungeonFloorSmaller$5", "dungeonFloorSmaller$6"),
		))}})
        .addAttribute(MapParamsAttribute(true, true))
}
Entities.add("DungeonStairsDown") {
	Entity()
		.addAttribute(TextureAttribute { position, random, textures -> run {
			textures add Textures.get("dungeonStairsDown")
		}})
		.addAttribute(MapParamsAttribute(false, true))
}
Entities.add("DungeonStairsUp") {
	Entity()
		.addAttribute(TextureAttribute { position, random, textures -> run {
            textures add Textures.get("dungeonStairsUp")
		}})
		.addAttribute(MapParamsAttribute(false, true))
}
Entities.add("StonePillar") {
	Entity()
		.addAttribute(TextureAttribute { position, random, textures -> run {
			textures add Textures.getRandomTexture(random, listOf(
				80f to listOf("stonePillar"),
				20f to listOf("stonePillarCracked"),
		))}})
		.addAttribute(MapParamsAttribute(false, false))
}
Entities.add("WoodenDoor") {
	Entity()
		.addAttribute(TextureAttribute { position, random, textures -> run {
            textures add Textures.get("woodenDoorClosed")
            textures add Textures.getOrNull(random, 30f, "woodenDoorClosed")
        }})
		.addAttribute(InteractionAttribute(arrayListOf(Interaction.DOOR())))
		.addAttribute(ChangesImpassableAttribute())
		.addAttribute(MapParamsAttribute(false, false))
}
Entities.add("DungeonWall") {
	Entity()
		.addAttribute(Identity.Wall())
		.addAttribute(TextureAttribute { position, random, textures -> run {
			textures.add(position!!.check(listOf(2), Identity.Wall::class, true) {
				Textures.getRandomTexture(random, 100f, listOf("dungeonWall$1", "dungeonWall$2", "dungeonWall$3", "dungeonWall$4", "dungeonWall$5"))}) ?:
                textures.add(Textures.get("dungeonWallInside"))
            textures.add(position!!.check(listOf(8), Identity.Wall::class, true) {
                Textures.get("dungeonWallTopInside")}) ?:
			textures.add(position!!.check(listOf(2 to NameOrIdentity(Identity.Wall::class, true), 4 to NameOrIdentity(Identity.Wall::class), 7 to NameOrIdentity(Identity.Wall::class)), true, false) {
				Textures.getRandomTexture(random, 100f, listOf("dungeonWallTop$1", "dungeonWallTop$2", "dungeonWallTop$3", "dungeonWallTop$4", "dungeonWallTop$5"))?.xy(0f, 16f)})
			textures.add(position!!.check(listOf(8), Identity.Wall::class, true) {
				Textures.getRandomTexture(random, 100f, listOf("dungeonWallTop$1", "dungeonWallTop$2", "dungeonWallTop$3", "dungeonWallTop$4", "dungeonWallTop$5"))?.xy(0f, 21f)})
            textures.add(position!!.check(listOf(4 to NameOrIdentity(Identity.Wall::class, true), 1 to NameOrIdentity(Identity.Wall::class), 2 to NameOrIdentity(Identity.Wall::class)), true, false, true) {
                Textures.getRandomTexture(random, 100f, listOf("dungeonWallSide$1", "dungeonWallSide$2", "dungeonWallSide$3", "dungeonWallSide$4", "dungeonWallSide$5"))?.xy(0f, 5f)})  // To have round corners, change those textures
            textures.add(position!!.check(listOf(2 to NameOrIdentity(Identity.Wall::class), 1 to NameOrIdentity(Identity.Wall::class, true)), true, false, true) {
                Textures.getRandomTexture(random, 100f, listOf("dungeonWallSide$1", "dungeonWallSide$2", "dungeonWallSide$3", "dungeonWallSide$4", "dungeonWallSide$5"))})
            textures.add(position!!.check(listOf(4, 8), Identity.Wall::class, true, true, false, true) {
                Textures.getRandomTexture(random, 100f, listOf("dungeonWallSideSmall$1", "dungeonWallSideSmall$2", "dungeonWallSideSmall$4", "dungeonWallSideSmall$5"))}) ?:
            textures.add(position!!.check(listOf(4, 2), Identity.Wall::class, true, true, false, true) {
                Textures.getRandomTexture(random, 100f, listOf("dungeonWallSideSmall$1", "dungeonWallSideSmall$2", "dungeonWallSideSmall$4", "dungeonWallSideSmall$5"))})
            textures.add(position!!.check(listOf(1 to NameOrIdentity(Identity.Wall::class, true), 2 to NameOrIdentity(Identity.Wall::class), 4 to NameOrIdentity(Identity.Wall::class), 7 to NameOrIdentity(Identity.Wall::class), 8 to NameOrIdentity(Identity.Wall::class)), true, false, true) {
                Textures.getRandomTexture(random, 100f, listOf("dungeonWallSideSmall$1", "dungeonWallSideSmall$2", "dungeonWallSideSmall$4", "dungeonWallSideSmall$5"))})
        }})
		.addAttribute(MapParamsAttribute(false, false))
}
