package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.Interaction

class InteractionAttribute(val interactionList: ArrayList<Interaction>): Attribute() {

    fun getPrimaryInteraction(): Interaction? {
        for (action in interactionList)
            if (action.isPrimary)
                return action
        return null
    }
}
