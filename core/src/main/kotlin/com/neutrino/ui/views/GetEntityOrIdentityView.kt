package com.neutrino.ui.views

import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisScrollPane
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.TextureButton
import com.neutrino.ui.views.util.Callback
import com.neutrino.ui.views.minor.IdentityButtonTable
import com.neutrino.util.getChangeListener
import ktx.actors.setScrollFocus

class GetEntityOrIdentityView(
    override val callback: (data: Triple<EntityOrIdentity, String, Boolean>) -> Unit
): VisWindow(""), Callback<Triple<GetEntityOrIdentityView.EntityOrIdentity, String, Boolean>> {

//    val builder = StandardTableBuilder()
    private var notButtonChecked = false
    private val notButton = TextureButton(Textures.get("notTexture"))
    private val identityButtonTable = IdentityButtonTable() { returnIdentity(it) }
    private val entitiesView = EntitiesView(false, false) { returnEntity(it) }

    init {
        isModal = true
        closeOnEscape()
        addCloseButton()
        notButton.setSize(64f, 64f)
        notButton.addListener(getChangeListener { _, _ ->
            notButtonChecked = !notButtonChecked
            if (!notButtonChecked)
                notButton.texture = Textures.get("notTexture")
            else
                notButton.texture = Textures.get("notActiveTexture")
        })
        val topTable = VisTable()
        topTable.add(notButton).left().padRight(16f)
        topTable.add(identityButtonTable)
        add(topTable).row()
        add(Separator()).padTop(16f).growX().row()
        val container = Container<VisScrollPane>()
        container.actor = getScrollPane(entitiesView)
        add(container).expandX().padTop(16f)

        pack()
        height = 1080 * 3/4f
        centerWindow()
    }

    private fun getScrollPane(table: VisTable): VisScrollPane {
        val entitiesScrollPane = VisScrollPane(table)
        entitiesScrollPane.setScrollFocus(true)
        entitiesScrollPane.setScrollingDisabled(true, false)
        entitiesScrollPane.setOverscroll(false, false)
        entitiesScrollPane.setScrollbarsVisible(true)
        entitiesScrollPane.setScrollbarsOnTop(true)
        return entitiesScrollPane
    }

    fun returnEntity(name: String) {
        super.close()
        callback.invoke(Triple(EntityOrIdentity.ENTITY, name, notButtonChecked))
    }

    fun returnIdentity(name: String) {
        super.close()
        callback.invoke(Triple(EntityOrIdentity.IDENTITY, name, notButtonChecked))
    }

    override fun close() {
        super.close()
        callback.invoke(Triple(EntityOrIdentity.NOTHING, name, notButtonChecked))
    }

    enum class EntityOrIdentity {
        ENTITY,
        IDENTITY,
        NOTHING
    }
}
