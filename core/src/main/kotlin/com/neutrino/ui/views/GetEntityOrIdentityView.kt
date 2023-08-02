package com.neutrino.ui.views

import com.kotcrab.vis.ui.building.StandardTableBuilder
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.ui.views.util.IdentityButtonTable
import com.neutrino.ui.views.util.Callback

class GetEntityOrIdentityView(
    override val callback: (data: Pair<EntityOrIdentity, String>) -> Unit
): VisWindow(""), Callback<Pair<GetEntityOrIdentityView.EntityOrIdentity, String>> {

    val builder = StandardTableBuilder()
    private val identityButtonTable = IdentityButtonTable() { returnIdentity(it) }
    private val entitiesView = EntitiesView(false) { returnEntity(it) }

    init {
        isModal = true
        closeOnEscape()
        addCloseButton()
        builder.append(identityButtonTable).row()
        // TODO add breakline
        builder.append(entitiesView)

        add(builder.build()).expand().fill()
        pack()
        centerWindow()
    }

    fun returnEntity(name: String) {
        close()
        callback.invoke(EntityOrIdentity.ENTITY to name)
    }

    fun returnIdentity(name: String) {
        close()
        callback.invoke(EntityOrIdentity.IDENTITY to name)
    }

    enum class EntityOrIdentity {
        ENTITY,
        IDENTITY
    }
}
