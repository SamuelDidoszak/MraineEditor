package com.neutrino.ui.elements

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.SnapshotArray
import com.kotcrab.vis.ui.widget.VisTable

class VisTableNested: VisTable() {

    private val tables = ArrayList<VisTable>(2)

    init {
        val newTable = VisTable()
        add(newTable).left()
        tables.add(newTable)
    }

    override fun invalidate() {
        super.invalidate()
        val actors = children.filter { it !is VisTable }
        for (actor in actors) {
            removeActor(actor)
            addNested(actor)
        }
    }

    fun addNested(actor: Actor) {
        val newRow = width <=
            tables.last().children.sumOf { it.width.toInt() } +
            actor.width

        if (newRow) {
            val newTable = VisTable()
            newTable.width = width
            row()
            add(newTable).left()
            tables.add(newTable)
        }

        tables.last().add(actor)
    }

    fun removeNested(actor: Actor) {
        for (i in tables.indices) {
            val table = tables[i]
            if (table.removeActor(actor)) {
                if (table.children.size == 0 && tables.size != 1) {
                    removeActor(table)
                    tables.remove(table)
                } else if (i != tables.size) {
                    refillTable()
                }
                break
            }
        }
    }

    fun removeLast() {
        val table = tables.last()
        table.removeActorAt(table.children.size - 1, false)
        if (table.children.size == 0 && tables.size != 1) {
            removeActor(table)
            tables.remove(table)
        }
    }

    private fun refillTable() {
        val actors = ArrayList<Actor>()
        actors.addAll(children.filter { it !is VisTable })
        for (table in tables) {
            for (actor in table.children)
                actors.add(actor)
        }
        if (tables.size > 1) {
            for (i in (1 until tables.size).reversed()) {
                removeActor(tables[i])
                tables.removeAt(i)
            }
        }
        for (actor in actors) {
            addNested(actor)
        }
    }

    fun getElements(): SnapshotArray<Actor?> {
        val elements = SnapshotArray<Actor?>()
        tables.forEach { elements.addAll(it.children) }
        return elements
    }
}
