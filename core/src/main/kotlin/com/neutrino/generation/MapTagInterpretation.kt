package com.neutrino.generation

import com.neutrino.util.EntityName

class MapTagInterpretation(val tagList: List<MapTag>) {
    lateinit var tileset: Tileset
    lateinit var mapGenerators: List<Generator>
    lateinit var characterList: List<EntityName>
    lateinit var itemList: List<Pair<Float, EntityName>>
    val tagParams: TagParams = TagParams(10f)

    init {
        if (tagList.isEmpty()) {
            tileset = Tileset()
            mapGenerators = ArrayList()
            characterList = ArrayList()
            itemList = ArrayList()
        } else if (tagList.size == 1) {
            tileset = tagList[0].tileset
            mapGenerators = tagList[0].mapGenerators
            characterList = tagList[0].characterList
            itemList = tagList[0].itemList
        } else {
            val tileset = Tileset()
            val mapGenerators: ArrayList<Generator> = ArrayList()
            val characterList: ArrayList<EntityName> = ArrayList()
            val itemList: ArrayList<Pair<Float, EntityName>> = ArrayList()
            for (tag in tagList) {
                tileset += tileset
                // Add generators
                for (generator in tag.mapGenerators) {
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
                    tagParams.mergeParamModifiers(tag.tagParams)
                else
                    tagParams.mergeParams(tag.tagParams)
            }
            this.tileset = tileset
            this.characterList = characterList
            this.itemList = itemList
        }
    }
}
