package com.neutrino.ui.elements

import com.kotcrab.vis.ui.widget.VisTextField

class TextField(text: String): VisTextField(text) {
    override fun getPrefWidth(): Float {
        return width
    }
}
