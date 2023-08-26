package com.neutrino.builders

import com.badlogic.gdx.Gdx
import com.neutrino.generation.Generators
import com.neutrino.ui.generators.GenerationAlgorithmView
import com.neutrino.ui.generators.methods.GeneratorMethodView

class GeneratorBuilder(val name: String, val main: Boolean, val priority: Int, val associations: List<String>) {

    private val generatorsFile = Gdx.files.local("assets/core/AddGenerators.kts")
    private val builder = StringBuilder(400)

    fun buildGenerator(generators: ArrayList<Pair<GenerationAlgorithmView, ArrayList<GeneratorMethodView>>>): GeneratorBuilder {
        builder.setLength(0)
        addHeader(name, main, priority, associations)
        for (generator in generators) {
            addIndicedLine()
            builder.append(generator.first.generateString())
            generator.second.forEach {
//                builder.append("\n")
                for (line in it.generateString().lines()) {
                    addIndicedLine(1)
                    builder.append(line)
                }
            }
            addIndicedLine(1)
            builder.append(".generateAll()")
        }
        builder.append("\n}")
        if (associations.isNotEmpty()) {
            builder.append(", ")
            addAssociations(associations)
        }
        builder.append(")")

        return this
    }

    fun addIndicedLine(indices: Int = 0) {
        builder.append("\n\t")
        for (i in 0 until indices)
            builder.append("\t")
    }

    private fun addHeader(name: String, main: Boolean, priority: Int, associations: List<String>) {
        if (!main && priority == Generators.DEFAULT_PRIORITY) {
            builder.append("Generators.add(\"$name\"")
            if (associations.isNotEmpty()) {
                builder.append(", ")
                addAssociations(associations)
            }
        }
        else
            builder.append("Generators.add(\"$name\", Generator($main, $priority")

        builder.append(") {")
    }

    private fun addAssociations(associations: List<String>) {
        builder.append("listOf(")
        associations.forEach {
            builder.append("$it, ")
        }
        builder.delete(builder.length - 2, builder.length)
        builder.append(")")
    }

    fun save() {
        writeToFile(name, builder.toString())
    }

    private fun writeToFile(generatorName: String, string: String) {
        val fileString = generatorsFile.readString()
        val generatorIndex = fileString.indexOf("Generators.add(\"$generatorName\"")
        val newLine = generatorIndex == -1 && (fileString.last() == ')' || fileString.last() == '}')
        if (generatorIndex == -1)
            addToFile(string, newLine)
        else
            replaceInFile(string, fileString, generatorIndex)
    }

    private fun addToFile(string: String, newLine: Boolean) {
        if (newLine)
            generatorsFile.writeString("\n", true)
        generatorsFile.writeString(string, true)
    }

    private fun replaceInFile(string: String, fileString: String, startIndex: Int) {
        var end = fileString.indexOf("Generators.add(", startIndex + 1)
        if (end == -1)
            end = fileString.length
        else
            end -= 1

        generatorsFile.writeString(fileString.replaceRange(startIndex, end, string), false)
    }
}
