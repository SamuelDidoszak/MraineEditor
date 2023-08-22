package com.neutrino.ui.views

import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.ui.views.util.Callback
import com.neutrino.ui.views.minor.EntitiesTable
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.ui.views.util.Search

class EntitiesView(
    allowEntityAddition: Boolean,
    allowEntityEditing: Boolean,
    override val callback: (data: String) -> Unit): VisTable(), Callback<String> {

    private val search: Search<EntityButton> = Search {
        entitiesTable.displaySearchResults(it)
    }
    private val entitiesTable = EntitiesTable(allowEntityAddition, allowEntityEditing, search, callback)

    init {
        padTop(0f)
        add(search).growX().padTop(16f).padBottom(16f).padLeft(8f).padRight(8f).row()
        add(entitiesTable).growX().left().row()
    }
}
