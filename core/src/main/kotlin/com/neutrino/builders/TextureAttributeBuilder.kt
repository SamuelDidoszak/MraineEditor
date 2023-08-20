package com.neutrino.builders

import com.neutrino.ui.attributes.TextureAttributeView

class TextureAttributeBuilder {

    private val builder = StringBuilder(300)

    fun generateString(textureTables: ArrayList<TextureAttributeView.TextureTable>): String {
        fun addIndicedLine(indices: Int = 0) {
            builder.append("\n\t\t")
            for (i in 0 until indices)
                builder.append("\t")
        }
        fun addBlock() {
//            builder.append(".also {if (it) return@run}")
            builder.append("?.also {return@run}")
        }

        fun getAnimationName(textureTable: TextureAttributeView.TextureTable): String {
            return textureTable.textures.keys.first().substringBefore('#')
        }
        fun addTextureListString(textureTable: TextureAttributeView.TextureTable) {
            if (textureTable.getAnimationState() != 0) {
                builder.append("listOf(\"${getAnimationName(textureTable)}\")")
                return
            }
            builder.append("listOf(")
            for (texture in textureTable.textures.keys) {
                builder.append("\"$texture\", ")
            }
            builder.delete(builder.length - 2, builder.length)
            builder.append(")")
        }
        fun addSingleTexture(textureTable: TextureAttributeView.TextureTable) {
            if (!textureTable.hasRequirements())
                builder.append("textures.add(")

            if (textureTable.getProbability().toInt() != 100)
                builder.append("Textures.getOrNull(random, ${textureTable.getProbability().toInt()}f, ")
            else
                builder.append("Textures.get(")

            if (textureTable.getAnimationState() == 0)
                builder.append("\"${textureTable.textures.keys.first()}\")")
            else
                builder.append("\"${getAnimationName(textureTable)}\")")
        }
        fun addSingleRandomTexture(textureTable: TextureAttributeView.TextureTable) {
            if (!textureTable.hasRequirements())
                builder.append("textures.add(")
            builder.append("Textures.getRandomTexture(random, ${textureTable.getProbability().toInt()}f, ")
            addTextureListString(textureTable)
            builder.append(")")
        }
        fun addMultipleRandomTextures(textureTable: TextureAttributeView.TextureTable) {
            builder.append("${textureTable.getProbability().toInt()}f to ")
            addTextureListString(textureTable)
            builder.append(",")
        }

        fun addSingleRequirementList(textureTable: TextureAttributeView.TextureTable) {
            builder.append("listOf(")
            for (i in 0 until 9) {
                if (textureTable.rules!![i] != null)
                    builder.append("${i + 1}, ")
            }
            builder.delete(builder.length - 2, builder.length)
            builder.append(")")
        }

        fun addRequirements(textureTable: TextureAttributeView.TextureTable) {
            val requirements = textureTable.rules!!.filterNotNull()

            fun isSame(): Boolean {
                var isSame = true
                val firstReq = requirements.first()
                for (req in requirements) {
                    if (req != firstReq) {
                        isSame = false
                        break
                    }
                }
                return isSame
            }

            var singleNameIdentity = isSame()

            if (singleNameIdentity) {
                builder.append("textures.add(position!!.check(")
                addSingleRequirementList(textureTable)
                val nameOrIdentity = requirements.first()
                val name =
                    nameOrIdentity.getEntityName() ?:
                    ("Identity." + nameOrIdentity.getIdentityName() + "::class")
                builder.append(", $name")
                if (nameOrIdentity.not)
                    builder.append(", true")
                if (textureTable.flipX || textureTable.flipY) {
                    if (!nameOrIdentity.not)
                        builder.append(", false")
                    builder.append(", ${textureTable.flipX}, ${textureTable.flipY}")
                }
                builder.append(") {")
            } else {
                builder.append("textures.add(position!!.check(listOf(")
                for (i in 0 until 8) {
                    if (textureTable.rules!![i] == null)
                        continue
                    builder.append("${i + 1} to ${textureTable.rules!![i]}, ")
                }
                builder.delete(builder.length - 2, builder.length)
                builder.append(")")
                if (textureTable.flipX || textureTable.flipY)
                    builder.append(", ${textureTable.flipX}, ${textureTable.flipY}")
                builder.append(") {")
            }
        }

        builder.append("TextureAttribute { position, random, textures -> run {")

        var block = false
        var chained = false
        var chainedTextureRequirements = false
        for (i in 0 until textureTables.size - 1) {
            val textureTable = textureTables[i]
            if (textureTable.textures.isEmpty())
                continue
            addIndicedLine(1)

            if (textureTable.isChained()) {
                if (!block && textureTable.isBlock())
                    block = true

                // it's a single texture out of a chain group
                if (!chained && (i in textureTables.indices && !textureTables[i + 1].isChained()) ||
                    (!chained && i == textureTables.size - 2)) {
                    if (textureTable.textures.size == 1 || textureTable.getAnimationState() != 0) {
                        val hasRequirements = textureTable.hasRequirements()
                        if (hasRequirements) {
                            addRequirements(textureTable)
                            addIndicedLine(2)
                        }
                        addSingleTexture(textureTable)
                        if (hasRequirements)
                            builder.append("}")
                        builder.append(")")
                        if (block)
                            addBlock()
                        continue
                    }
                    val hasRequirements = textureTable.hasRequirements()
                    if (hasRequirements) {
                        addRequirements(textureTable)
                        addIndicedLine(2)
                    }
                    addSingleRandomTexture(textureTable)
                    if (hasRequirements)
                        builder.append("}")
                    builder.append(")")
                    if (block)
                        addBlock()
                    continue
                }
                if (!chained) {
                    if (textureTable.hasRequirements()) {
                        addRequirements(textureTable)
                        addIndicedLine(2)
                        chainedTextureRequirements = true
                    } else
                        builder.append("textures.add(")
                    builder.append("Textures.getRandomTexture(random, listOf(")
                    addIndicedLine(1)
                }
                builder.append("\t")
                if (chainedTextureRequirements)
                    builder.append("\t")
                chained = true
                addMultipleRandomTextures(textureTable)
            } else {
                if (chained) {
                    builder.append("\t")
                    if (chainedTextureRequirements)
                        builder.append("\t")
                    builder.append("))")
                    if (chainedTextureRequirements)
                        builder.append("}")
                    builder.append(")")
                    if (block)
                        addBlock()

                    addIndicedLine(1)
                }
                chained = false
                chainedTextureRequirements = false
                block = textureTable.isBlock()
                if (textureTable.hasRequirements()) {
                    addRequirements(textureTable)
                    addIndicedLine(2)
                }
                if (textureTable.textures.size == 1 || textureTable.getAnimationState() != 0)
                    addSingleTexture(textureTable)
                else
                    addSingleRandomTexture(textureTable)
                if (textureTable.hasRequirements())
                    builder.append("}")
                builder.append(")")
                if (block)
                    addBlock()
            }
        }

        if (chained) {
            addIndicedLine(1)
            builder.append("))")
            if (!chainedTextureRequirements)
                builder.append(")")
        } else
            addIndicedLine()
        if (chainedTextureRequirements)
            builder.append("})")
        builder.append("}}")
        return builder.toString()
    }
}
