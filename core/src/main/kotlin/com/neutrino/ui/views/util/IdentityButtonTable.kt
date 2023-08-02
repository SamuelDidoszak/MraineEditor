package com.neutrino.ui.views.util

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.entities.attributes.Identity
import com.neutrino.ui.elements.VisTableNested
import com.neutrino.util.getChangeListener

class IdentityButtonTable(override val callback: (data: String) -> Unit): VisTable(), Callback<String> {

    private val identityList = Identity::class.nestedClasses.map { it.simpleName }
    private val identityTable = VisTableNested()

    init {
        identityTable.width = 420f
        identityTable.padLRTB(4f, 4f, 2f, 2f)
        add(identityTable).expandX()
        identityList.forEach { identityTable.addNested(getIdentityButton(it!!)) }
    }

    private fun getIdentityButton(name: String): TextButton {
        return VisTextButton(name, getChangeListener { _, _ -> callback.invoke(name) })
    }
}
