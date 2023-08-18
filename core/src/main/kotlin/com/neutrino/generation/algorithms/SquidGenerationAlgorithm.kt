package com.neutrino.generation.algorithms

import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.Tileset
import com.neutrino.generation.util.GenerationParams
import com.neutrino.generation.util.ModifyMap
import com.neutrino.util.EntityName
import com.neutrino.util.hasIdentity
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.styled.TilesetType
import squidpony.squidmath.Coord
import squidpony.squidmath.GWTRNG
import squidpony.squidmath.IRNG

class SquidGenerationAlgorithm(
    val tilesetType: TilesetType,
    params: GenerationParams,
    sizeX: Int = params.map[0].size,
    sizeY: Int = params.map.size,
    modifyBaseMap: ModifyMap? = null
): GenerationAlgorithm(params, sizeX, sizeY, modifyBaseMap) {

    override val MAIN: Boolean = true
    override val GENERATION_PRIORITY: Int = 0

    private val irng: IRNG = GWTRNG(params.rng.nextLong())
    private val dungeonGenerator = DungeonGenerator(sizeX, sizeY, irng)
    private lateinit var dungeonLayout: Array<out CharArray>

    override fun generate(tileset: Tileset): List<List<MutableList<Entity>>> {
        // TODO mix interpretedTags and entityIdentities
        tileset += params.interpretedTags.tileset
        generateDungeon(tilesetType)
        println(tileset.getEntity(Identity.Wall()))
        setWalls(map, tileset.getRandomEntity(Identity.Wall(), params.rng)!!)
        addEntities(tileset.getRandomEntity(Identity.Floor(), params.rng)!!, listOf(), 1f)
        return map
    }

    private fun generateDungeon(tilesetType: TilesetType) {
        dungeonLayout = dungeonGenerator.addDoors(100, true)
            .generate(tilesetType)
    }

    private fun setWalls(map: List<List<MutableList<Entity>>>, wall: EntityName) {
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                if (dungeonLayout[y][x] == '#')
                    map[y][x].add(Entities.new(wall))
            }
        }
    }

    fun setEntrances(map: List<List<MutableList<Entity>>>, down: EntityName, up: EntityName) {
        val stairsDown = correctStairs(map, dungeonGenerator.stairsDown)
        val stairsUp = correctStairs(map, dungeonGenerator.stairsUp)

        map[stairsDown.y][stairsDown.x].add(Entities.new(down))
        map[stairsUp.y][stairsUp.x].add(Entities.new(up))
    }

    private fun correctStairs(map: List<List<MutableList<Entity>>>, stairs: Coord): Coord {
        var stairsCorrected: Coord? = if (map[stairs.y][stairs.x] hasIdentity Identity.Wall::class) null else stairs

        for (y in -1..1) {
            if (stairsCorrected != null)
                break
            if (stairs.y + y !in map.indices)
                continue
            for (x in -1..1) {
                if (stairs.x + x !in map[stairs.y].indices)
                    continue

                if (!map[stairs.y + y][stairs.x + x].hasIdentity(Identity.Wall::class)) {
                    stairsCorrected = Coord.get(stairs.x + x, stairs.y + y)
                    break
                }
            }
        }

        if (stairsCorrected != null)
            return stairsCorrected

        // randomize position if no coord fount
        do {
            stairsCorrected = Coord.get(
                params.rng.nextInt(0, sizeX),
                params.rng.nextInt(0, sizeY)
            )
        } while (map[stairsCorrected!!.y][stairsCorrected.x] hasIdentity Identity.Wall::class)

        return stairsCorrected
    }
}
