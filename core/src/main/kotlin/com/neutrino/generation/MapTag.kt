package com.neutrino.generation

import com.neutrino.util.EntityName

data class MapTag(
    val tileset: Tileset,
    val mapGenerators: List<Generator>,
    val characterList: List<EntityName>,
    val itemList: List<Pair<Float, EntityName>>,
    val tagParams: TagParams,
    val isModifier: Boolean
)
