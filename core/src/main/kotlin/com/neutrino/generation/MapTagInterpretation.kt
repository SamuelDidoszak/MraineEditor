package com.neutrino.generation

import com.neutrino.entities.attributes.Identity
import com.neutrino.generation.algorithms.GenerationAlgorithm
import com.neutrino.util.EntityName
import kotlin.reflect.KClass

class MapTagInterpretation(val tagList: List<MapTag>) {
    lateinit var entityIdentities: List<EntityIdentity>
    lateinit var mapGenerators: List<Pair<Float, KClass<out GenerationAlgorithm>>>
    lateinit var characterList: List<EntityName>
    lateinit var itemList: List<Pair<Float, EntityName>>
    val generationParams: GenerationParams = tagList[0].generationParams

    init {
        if (tagList.size == 1) {
            entityIdentities = tagList[0].entityIdentities
            mapGenerators = tagList[0].mapGenerators
            characterList = tagList[0].characterList
            itemList = tagList[0].itemList
        } else {
            val entityIdentities: ArrayList<EntityIdentity> = ArrayList()
            val mapGenerators: ArrayList<Pair<Float, KClass<out GenerationAlgorithm>>> = ArrayList()
            val characterList: ArrayList<EntityName> = ArrayList()
            val itemList: ArrayList<Pair<Float, EntityName>> = ArrayList()
            for (tag in tagList) {
                // Add entityIdentities
                for (EI in tag.entityIdentities) {
                    var canAdd = true
                    for (addedEI in entityIdentities) {
                        if (EI == addedEI) {
                            canAdd = true
                            break
                        }
                    }
                    if (canAdd)
                        entityIdentities.add(EI)
                }
                // Add generators
                for (generator in tag.mapGenerators.sortedBy { it.first }) {
                    var canAdd = true
                    for (addedGenerator in mapGenerators) {
                        if (generator == addedGenerator) {
                            canAdd = true
                            break
                        }
                    }
                    if (canAdd)
                        mapGenerators.add(generator)
                }
                // Add characters
                for (character in tag.characterList) {
                    var canAdd = true
                    for (addedCharacter in characterList) {
                        if (character == addedCharacter) {
                            canAdd = true
                            break
                        }
                    }
                    if (canAdd)
                        characterList.add(character)
                }
                // Add items
                for (item in tag.itemList) {
                    var canAdd = true
                    for (addedItem in itemList) {
// TODO
//                        if (item.value == addedItem.value) {
//                            canAdd = true
//                            break
//                        }
                    }
                    if (canAdd)
                        itemList.add(item)
                }
                // Generate params
                if (tag.isModifier)
                    generationParams.mergeParamModifiers(tag.generationParams)
                else
                    generationParams.mergeParams(tag.generationParams)
            }
            this.entityIdentities = entityIdentities
            this.characterList = characterList
            this.itemList = itemList
        }
    }

    fun getEntityIdentity(identity: KClass<out Identity>): EntityIdentity? {
        for (EI in entityIdentities) {
            if (EI.identity == identity)
                return EI
        }
        return null
    }
}
