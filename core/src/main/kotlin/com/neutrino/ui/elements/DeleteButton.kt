package com.neutrino.ui.elements

import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.util.Constants
import ktx.actors.onClick

class DeleteButton(onClick: () -> Unit): VisTextButton("X", Constants.VisUIX1Skin.get(VisTextButtonStyle::class.java)) {
    init {
        onClick { onClick.invoke() }
    }
}
