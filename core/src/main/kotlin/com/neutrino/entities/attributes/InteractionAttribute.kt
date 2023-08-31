package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.Interaction
import com.neutrino.entities.util.RequiresEntityParameter

class InteractionAttribute(interactionList: ArrayList<Interaction>): Attribute() {

    val interactionList = object : ArrayList<Interaction>() {
        override fun add(element: Interaction): Boolean {
             if (element is RequiresEntityParameter)
                 element.entity = entity
            return super.add(element)
        }
        override fun add(index: Int, element: Interaction) {
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

    fun getPrimaryInteraction(): Interaction? {
        for (action in interactionList)
            if (action.isPrimary)
                return action
        return null
    }
}
