package com.neutrino.generation.algorithms

import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.MapParamsAttribute
import com.neutrino.generation.*
import com.neutrino.util.EntityName
import com.neutrino.util.lessThanDelta
import kotlin.math.roundToInt
import kotlin.random.Random

abstract class GenerationAlgorithm(
    val interpretedTags: MapTagInterpretation,
    val rng: Random,
    map: List<List<MutableList<Entity>>>?,
    val sizeX: Int = map!![0].size, val sizeY: Int = map!!.size) {

    abstract val MAIN: Boolean
    abstract val GENERATION_PRIORITY: Int
    val map: List<List<MutableList<Entity>>> = map ?: getEmptyMap()

    open fun generate(
        entityIdentities: List<EntityIdentity> = interpretedTags.entityIdentities
    ): List<List<MutableList<Entity>>> {
        return map
    }

    open fun generate(
        rules: List<EntityPositionRequirement>
    ): List<List<MutableList<Entity>>> {
        return map
    }

    private fun getEmptyMap(): List<List<MutableList<Entity>>> {
        val list = arrayListOf<ArrayList<MutableList<Entity>>>()
        for (y in 0 until sizeY) {
            list.add(arrayListOf())
            for (x in 0 until sizeX) {
                list[y].add(mutableListOf())
            }
        }
        return list
    }

    fun addEntities(entity: EntityName, entityPositionRequirementList: List<EntityPositionRequirement>, probability: Float,
                    replaceUnderneath: Boolean = false, assertAmount: Boolean = false) {
        val fulfillingTileList: MutableList<Pair<Int, Int>> = ArrayList()
        val entityChecker = NameOrIdentity(entity)
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                var generationAllowed = true
                for (mapEntity in map[y][x]) {
                    if (!mapEntity.get(MapParamsAttribute::class)!!.allowOnTop) {
                        generationAllowed = false
                        break
                    }
                }
                if (!generationAllowed)
                    continue

                // Interpret requirements
                // Variable needed to interpret remaining requirements as grouped
                var groupRequirementType: EntityPositionRequirementType? = null
                for (requirement in entityPositionRequirementList) {
                    // Group initiator
                    if (requirement.requirementList.isEmpty()) {
                        if (when(groupRequirementType) {
                                EntityPositionRequirementType.AND -> generationAllowed
                                EntityPositionRequirementType.NAND -> generationAllowed
                                EntityPositionRequirementType.NOR -> generationAllowed
                                EntityPositionRequirementType.OR, null -> false
                            })
                            break

                        groupRequirementType = requirement.requirementType
                        generationAllowed = true
                        continue
                    }
                    // Iterate until the next group
                    if (!generationAllowed)
                        continue

                    for (pair in requirement.requirementList) {
                        var entityUnder = true
                        when (pair.first) {
                            1 -> {
                                if (x == 0 || y == sizeY - 1)
                                    break
                                if (!checkMapForEntity(y + 1, x - 1, pair.second))
                                    entityUnder = false
                            }
                            2 -> {
                                if (y == sizeY - 1)
                                    break
                                if (!checkMapForEntity(y + 1, x, pair.second))
                                    entityUnder = false
                            }
                            3 -> {
                                if (x == sizeY - 1 || y == sizeY - 1)
                                    break
                                if (!checkMapForEntity(y + 1, x + 1, pair.second))
                                    entityUnder = false
                            }
                            4 -> {
                                if (x == 0)
                                    break
                                if (!checkMapForEntity(y, x - 1, pair.second))
                                    entityUnder = false
                            }
                            5 -> {
                                if (!checkMapForEntity(y, x, pair.second))
                                    entityUnder = false
                            }
                            6 -> {
                                if (x == sizeX - 1)
                                    break
                                if (!checkMapForEntity(y, x + 1, pair.second))
                                    entityUnder = false
                            }
                            7 -> {
                                if (x == 0 || y == 0)
                                    break
                                if (!checkMapForEntity(y - 1, x - 1, pair.second))
                                    entityUnder = false
                            }
                            8 -> {
                                if (y == 0)
                                    break
                                if (!checkMapForEntity(y - 1, x, pair.second))
                                    entityUnder = false
                            }
                            9 -> {
                                if (x == sizeX - 1 || y == 0)
                                    break
                                if (!checkMapForEntity(y - 1, x + 1, pair.second))
                                    entityUnder = false
                            }
                        }
                        when (requirement.requirementType) {
                            EntityPositionRequirementType.AND -> {
                                if (!entityUnder) {
                                    generationAllowed = false
                                    break
                                }
                            }
                            EntityPositionRequirementType.NAND -> {
                                if (!entityUnder) {
                                    generationAllowed = true
                                    break
                                } else
                                    generationAllowed = false
                            }
                            EntityPositionRequirementType.OR -> {
                                if (entityUnder) {
                                    generationAllowed = true
                                    break
                                } else
                                    generationAllowed = false
                            }
                            EntityPositionRequirementType.NOR -> {
                                if (entityUnder) {
                                    generationAllowed = false
                                    break
                                }
                            }
                        }
                    }
                    when (groupRequirementType) {
                        // not grouped, all of the requirements have to be passed
                        null -> {
                            if (!generationAllowed)
                                continue
                        }
                        EntityPositionRequirementType.AND -> {
                            if (!generationAllowed)
                                continue
                        }
                        EntityPositionRequirementType.NAND -> {
                            if (!generationAllowed) {
                                generationAllowed = true
                                break
                            }
                        }
                        EntityPositionRequirementType.OR -> {
                            if (generationAllowed)
                                break

                            if (requirement != entityPositionRequirementList.last())
                                generationAllowed = true
                        }
                        EntityPositionRequirementType.NOR -> {
                            if (generationAllowed) {
                                generationAllowed = false
                                continue
                            }

                            generationAllowed = true
                        }
                    }
                }
                // Add this tile to the list and generate later
                if (assertAmount && generationAllowed) {
                    fulfillingTileList.add(Pair(y, x))
                }

                // Generate entity if allowed with a probability
                else if (generationAllowed && rng.nextFloat().lessThanDelta(probability)) {
                    if (replaceUnderneath)
                        map[y][x].removeAll { true }
                    // Assert that the entity wasn't added already
                    if (!checkMapForEntity(y, x, entityChecker))
                        map[y][x].add(Entities.new(entity))
                }
            }
        }
        // generate certain amount of items
        // treat probability as amount
        if (assertAmount) {
            var generatedAmount = 0
            val max = if (probability >= fulfillingTileList.size) fulfillingTileList.size else probability.roundToInt()
            while (generatedAmount < max) {
                val index = rng.nextInt(fulfillingTileList.size)
                map[fulfillingTileList[index].first][fulfillingTileList[index].second].add(Entities.new(entity))
                fulfillingTileList.removeAt(index)
                generatedAmount++
            }
        }
    }

    fun checkMapForEntity(y: Int, x: Int, requiredEntity: NameOrIdentity): Boolean {
        for (mapEntity in map[y][x]) {
            if (requiredEntity.isSame(mapEntity))
                return true
        }
        return false
    }
}
