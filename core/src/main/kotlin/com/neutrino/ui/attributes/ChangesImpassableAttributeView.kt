package com.neutrino.ui.attributes

import com.kotcrab.vis.ui.widget.VisTable

class ChangesImpassableAttributeView: AttributeView(VisTable()) {

    override val attributeName: String = "ChangesImpassable"

    override fun generateString(): String {
        return "ChangesImpassable()"
    }
}
