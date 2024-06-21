package com.neutrino.ui.views

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter
import com.neutrino.ui.views.minor.TextureAtlasTable
import com.neutrino.ui.views.util.EntityButton
import com.neutrino.ui.views.util.Search

class TexturesView(): VisTable() {

    private val search: Search<EntityButton> = Search {
        if (findActor<VisTable>("atlasTable") == null) return@Search
        findActor<TextureAtlasTable>("atlasTable").displaySearchResults(it)
    }

    init {
        pad(0f)
        val tabsPane = initializeTabs()
        add(tabsPane.table).growX().left().row()
        add(search).growX().padTop(16f).padBottom(16f).padLeft(8f).padRight(8f).row()

        tabsPane.switchTab(0)
    }

    private fun initializeTabs(): TabbedPane {
        val folder = Gdx.files.absolute("${Gdx.files.localStoragePath}/textureSources/")
        val tabsPane = TabbedPane()

        folder.list().filterNot { it.name() == "temp" }.forEach {
            tabsPane.add(TextureTab(it.name()))
        }

        tabsPane.addListener(object : TabbedPaneAdapter() {
            override fun switchedTab(p0: Tab?) {
                if (p0 == null) return
                setActiveTab(p0.tabTitle)
            }
        })

        return tabsPane
    }

    fun setActiveTab(name: String) {
        if (findActor<VisTable>("atlasTable") != null)
            removeActor(findActor("atlasTable"))
        search.data.clear()
        val table = TextureAtlasTable(name, search)
        table.name = "atlasTable"
        add(table).growX().left().row()
    }

    private class TextureTab(val name: String): Tab(false, false) {
        private val table = Table()
        init {
            table.add(VisLabel(name))
        }
        override fun getTabTitle(): String = name
        override fun getContentTable(): Table = table
    }
}
