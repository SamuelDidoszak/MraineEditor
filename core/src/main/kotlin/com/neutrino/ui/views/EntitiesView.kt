package com.neutrino.ui.views

import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.ui.views.util.EntitiesTable
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.ui.views.util.Search

class EntitiesView: VisTable() {

    private val search: Search<EntityButton> = Search {
        entitiesTable.displaySearchResults(it)
    }
    private val entitiesTable = EntitiesTable(search)

    init {
        padTop(0f)
        add(search).growX().padTop(16f).padBottom(16f).padLeft(8f).padRight(8f).row()
        add(entitiesTable).growX().left().row()
    }
}
