package com.neutrino.builders

import com.badlogic.gdx.Gdx
import com.neutrino.ui.views.minor.AddGenerationRequirementsView

class GenerationRequirementBuilder {

    private val generatorsFile = Gdx.files.local("assets/core/AddGenerationRequirements.kts")
    private val builder = StringBuilder(40)
    private var additionString = ""

    fun buildRules(addRules: AddGenerationRequirementsView): GenerationRequirementBuilder {
        builder.setLength(0)
        if (addRules.saveIdentity != null)
            additionString = "GenerationRequirements.add(Identity.${addRules.saveIdentity!!::class.simpleName}(), listOf("
        else if (addRules.saveEntity != null)
            additionString = "GenerationRequirements.add(\"${addRules.saveEntity!!}\", listOf("
        else if (addRules.saveName != null)
            additionString = "GenerationRequirements.addOther(\"${addRules.saveName!!}\", listOf("
        builder.append(additionString)
        for (rule in addRules.getRules()) {
            builder.append("\n\t$rule,")
        }
        builder.append("\n))")

        return this
    }

    fun save() {
        val fileString = generatorsFile.readString()
        val ruleIndex = fileString.indexOf(additionString)
        val newLine = ruleIndex == -1 && (fileString.last() == ')')
        if (ruleIndex == -1)
            addToFile(newLine)
        else
            replaceInFile(fileString, ruleIndex)
    }

    private fun addToFile(newLine: Boolean) {
        if (newLine)
            generatorsFile.writeString("\n", true)
        generatorsFile.writeString(builder.toString(), true)
    }

    private fun replaceInFile(fileString: String, startIndex: Int) {
        var end = fileString.indexOf("GenerationRequirements.add", startIndex + 1)
        if (end == -1)
            end = fileString.length
        else
            end -= 1

        generatorsFile.writeString(fileString.replaceRange(startIndex, end, builder.toString()), false)
    }
}
