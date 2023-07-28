package com.neutrino.ui.views.util

import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextField

class Search<T>(
    private val onSearch: (data: List<T>) -> Unit
): VisTable() {

    private val searchPane = VisTextField()
    val data: ArrayList<Pair<T, String>> = object : ArrayList<Pair<T, String>>() {
        override fun add(element: Pair<T, String>): Boolean {
            return super.add(Pair(
                    element.first,
                    element.second.lowercase().filter { !it.isWhitespace() }))
        }

        override fun set(index: Int, element: Pair<T, String>): Pair<T, String> {
            return super.set(index, Pair(
                element.first,
                element.second.lowercase().filter { !it.isWhitespace() }))
        }
    }

    init {
        add(searchPane).growX()
        searchPane.setTextFieldListener { textField, c ->  searchThroughData(textField.text)}
    }

    private fun searchThroughData(searchQuery: String) {
        val regex = Regex(".*" +
            searchQuery.lowercase().filter { !it.isWhitespace() } +
            ".*")

        onSearch.invoke(data.filter { regex.matches(it.second) }.map { it.first })
    }
}
