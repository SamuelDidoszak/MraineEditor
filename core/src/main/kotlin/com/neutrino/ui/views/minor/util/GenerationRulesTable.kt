package com.neutrino.ui.views.minor.util

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisWindow
import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.generation.EntityPositionRequirementType
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.SingleEntityDrawer
import com.neutrino.textures.TextureSprite
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.views.GetEntityOrIdentityView
import com.neutrino.ui.views.util.Callback
import com.neutrino.util.getChangeListener
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.*
import kotlin.reflect.KClass

class GenerationRulesTable(
    var nameOrIdentity: NameOrIdentity? = null,
    var ruleType: EntityPositionRequirementType = EntityPositionRequirementType.AND,
    positions: Set<Int> = setOf(),
    override val callback: (data: Triple<EntityPositionRequirementType, NameOrIdentity?, Pair<List<Int>, RulePickerButton>>) -> Unit
) : VisWindow(""), Callback<Triple<EntityPositionRequirementType, NameOrIdentity?, Pair<List<Int>, RulePickerButton>>> {

    private val RULES_SIZE = 200f
    private val pickEntity = Container<Actor>()
    private val rulesButton = RulePickerButton(Textures.get("rulesTexture"))
    private val rules = mutableSetOf<Int>()
    var blockCenter = true

    init {
        rulesButton.setSize(RULES_SIZE, RULES_SIZE)
        rulesButton.pad = 0f
        rulesButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                addRule(pixelToPosition(y) * 3 + pixelToPosition(x))
                super.clicked(event, x, y)
            }
        })
        rulesButton.addListener(object : ClickListener(Input.Buttons.RIGHT) {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val position = (pixelToPosition(y) * 3 + pixelToPosition(x))
                rulesButton.setTexture(position, null as TextureSprite?)
                rules.remove(position)
                rulesButton.setNot(position, false)
                super.clicked(event, x, y)
            }
        })
        add(rulesButton)
        for (position in positions) {
            addRule(position)
        }
        addPropertiesTable()

        val saveButton = VisTextButton("save")
        saveButton.addListener(getChangeListener { _, _ ->
            callback.invoke(Triple(ruleType, nameOrIdentity, rules.toList() to rulesButton))
            close()
        })
        val closeButton = VisTextButton("close")
        closeButton.addListener(getChangeListener { _, _ -> close() })

        add(scene2d.visTable {
            add(closeButton).padRight(48f)
            add(saveButton)
        }).center().padTop(16f).padBottom(16f)

        pack()
        centerWindow()
    }

    private fun addPropertiesTable() {
        val propertiesTable = VisTable()

        propertiesTable.add(scene2d.visTextButton(ruleType.name).apply { onClick {
            scene2d.popupMenu {
                menuItem("AND").onClick {
                    this@apply.setText("AND")
                    ruleType = EntityPositionRequirementType.AND
                }
                menuItem("NAND").onClick {
                    this@apply.setText("NAND")
                    ruleType = EntityPositionRequirementType.NAND
                }
                menuItem("OR").onClick {
                    this@apply.setText("OR")
                    ruleType = EntityPositionRequirementType.OR
                }
                menuItem("NOR").onClick {
                    this@apply.setText("NOR")
                    ruleType = EntityPositionRequirementType.NOR
                }
            }.showMenu(stage, this)
        } }).expandX().center().padBottom(16f).row()

        propertiesTable.add(scene2d.visLabel("Requirement")).expandX().center().padBottom(4f).row()
        pickEntity.size(64f)
        pickEntity.actor = scene2d.visTextButton("+") {
            width = 64f
            height = 64f
            onClick {
                stage.addActor(GetEntityOrIdentityView { setNameOrIdentity(it.first, it.second) })
            }
        }
        propertiesTable.add(pickEntity).expandX().center().padBottom(16f).row()

        add(propertiesTable).padLeft(16f).padRight(16f).expandX().center().row()
    }

    private fun setNameOrIdentity(type: GetEntityOrIdentityView.EntityOrIdentity, value: String) {
        if (type == GetEntityOrIdentityView.EntityOrIdentity.NOTHING)
            return
        if (type == GetEntityOrIdentityView.EntityOrIdentity.IDENTITY)
            nameOrIdentity = NameOrIdentity(Identity::class.nestedClasses
                .find { it.simpleName == value }!! as KClass<out Identity>)
        else if (type == GetEntityOrIdentityView.EntityOrIdentity.ENTITY)
            nameOrIdentity = NameOrIdentity(value)

        rules.forEach { addRule(it) }
    }

    private fun pixelToPosition(pixel: Float): Int {
        val ruleSize = RULES_SIZE / 3
        return (pixel / ruleSize).toInt()
    }

    private fun isNot(): Boolean {
        return when (ruleType) {
            EntityPositionRequirementType.AND -> false
            EntityPositionRequirementType.OR -> false
            EntityPositionRequirementType.NAND -> true
            EntityPositionRequirementType.NOR -> true
        }
    }

    private fun addRule(position: Int) {
        if (blockCenter && position == 4)
            return
        if (nameOrIdentity == null) {
            rulesButton.setTexture(position, null as TextureSprite?)
            rules.remove(position)
            rulesButton.setNot(position, isNot())
            return
        }
        if (nameOrIdentity!!.identity != null) {
            rulesButton.setTexture(position,
                Textures.get(nameOrIdentity!!.identity!!.simpleName!!.lowercase() + "Identity"))
            rulesButton.setNot(position, isNot())
            rules.add(position)
            return
        }
        if (nameOrIdentity!!.id != null) {
            val entity = Entities.new(nameOrIdentity!!.id!!)
            SingleEntityDrawer(entity)
            rulesButton.setTexture(position,
                entity.get(TextureAttribute::class)!!.textures.maxBy { it.width() + it.height() })
            rulesButton.setNot(position, isNot())
            rules.add(position)
            return
        }
    }
}
