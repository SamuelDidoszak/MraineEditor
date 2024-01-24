package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.InteractionType
import com.neutrino.entities.util.RequiresEntityParameter

class Interaction(interactionList: ArrayList<InteractionType>): Attribute() {

    val interactionList = object : ArrayList<InteractionType>() {
        override fun add(element: InteractionType): Boolean {
             if (element is RequiresEntityParameter)
                 element.entity = entity
            return super.add(element)
        }
        override fun add(index: Int, element: InteractionType) {
            if (element is RequiresEntityParameter)
                element.entity = entity
            return super.add(index, element)
        }
    }

    init {
        this.interactionList.addAll(interactionList)
    }

    override fun onEntityAttached() {
        for (interaction in interactionList) {
            if (interaction is RequiresEntityParameter)
                interaction.entity = entity
        }
    }

    fun getPrimaryInteraction(): InteractionType? {
        for (action in interactionList)
            if (action.isPrimary)
                return action
        return null
    }
}
