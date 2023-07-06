package com.neutrino.entities.attributes

import attributes.Attribute
import com.neutrino.entities.util.Interaction

class InteractionAttribute(val interactionList: ArrayList<Interaction>): Attribute() {

    override fun onEntityAttached() {
        for (interaction in interactionList) {
            setEntity(interaction)
        }
    }

    private fun setEntity(interaction: Interaction) {
        try {
            interaction::class.java.getField("entity").set(interaction, entity)
        } catch (_: Error) {}
    }

    fun getPrimaryInteraction(): Interaction? {
        for (action in interactionList)
            if (action.isPrimary)
                return action
        return null
    }
}
