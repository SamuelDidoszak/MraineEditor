package com.neutrino.builders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.kotcrab.vis.ui.widget.VisTextButton
import com.neutrino.entities.Entity
import com.neutrino.ui.attributes.AttributeView
import com.neutrino.ui.elements.VisTableNested

class EntityBuilder {

    private val entitiesFile = Gdx.files.local("assets/core/AddEntities.kts")
    private val builder = StringBuilder(300)

    fun build(name: String,
                      identityTable: VisTableNested,
                      addedAttributes: Set<AttributeView>,
                      editEntity: Entity? = null): String {
        if (editEntity == null && entitiesFile.file().readText().last() == '}')
            builder.append("\n")
        builder.append("Entities.add(\"$name\") {\n\tEntity()\n")

        val identities = identityTable.getElements().map { (it as? VisTextButton)?.text.toString() }
        if (identities.size > 1)
            builder.append("\t\t")
        for (i in 0 until identities.size - 1) {
            builder.append(".addAttribute(Identity.${identities[i]}())")
        }
        if (identities.size > 1)
            builder.append("\n")

        for (attribute in addedAttributes) {
            attribute.onSaveAction()
            builder.append("\t\t.addAttribute(")
            builder.append(attribute.generateString())
            builder.append(")\n")
        }
        builder.append("}")

        writeToFile(entitiesFile, builder.toString(), editEntity)
        return builder.toString()
    }

    private fun writeToFile(entitiesFile: FileHandle, string: String, editEntity: Entity?) {
        if (editEntity == null)
            addToFile(entitiesFile, string)
        else
            replaceInFile(entitiesFile, editEntity, string)
    }

    private fun addToFile(entitiesFile: FileHandle, string: String) {
        entitiesFile.writeString(string, true)
    }

    private fun replaceInFile(entitiesFile: FileHandle, oldEntity: Entity, string: String) {
        val fileContents = entitiesFile.readString()
        val start = fileContents.indexOf("Entities.add(\"${oldEntity.name}\")")
        var end = fileContents.indexOf("Entities.add(", start + 1)
        if (end == -1)
            end = fileContents.length
        else
            end -= 1

        entitiesFile.writeString(fileContents.replaceRange(start, end, string), false)
    }
}
