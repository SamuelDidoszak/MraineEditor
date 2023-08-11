package com.neutrino.ui.views.util

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.neutrino.entities.Entities
import com.neutrino.entities.attributes.Identity
import com.neutrino.entities.attributes.TextureAttribute
import com.neutrino.generation.NameOrIdentity
import com.neutrino.textures.SingleEntityDrawer
import com.neutrino.textures.Textures
import com.neutrino.ui.elements.RulePickerButton
import com.neutrino.ui.views.GetEntityOrIdentityView
import kotlin.reflect.KClass

class RulesTable(ruleList: MutableList<NameOrIdentity?>? = null) : VisTable() {

    private val RULES_SIZE = 400f
    private val rulesButton = RulePickerButton(Textures.get("rulesTexture"))
    private val rules = ruleList ?: MutableList<NameOrIdentity?>(9) {null}
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
        add(rulesButton)
        if (ruleList != null) {
            for (i in ruleList.indices) {
                if (ruleList[i] == null)
                    continue
                addRuleData(i, Triple(
                    if (ruleList[i]!!.identity != null)
                        GetEntityOrIdentityView.EntityOrIdentity.IDENTITY
                    else
                        GetEntityOrIdentityView.EntityOrIdentity.ENTITY,
                    if (ruleList[i]!!.identity != null)
                        ruleList[i]!!.getIdentityName()!!
                    else
                        ruleList[i]!!.getEntityName()!!,
                    ruleList[i]!!.not
                )
                )
            }
        }
    }

    private fun pixelToPosition(pixel: Float): Int {
        val ruleSize = RULES_SIZE / 3
        return (pixel / ruleSize).toInt()
    }

    private fun addRule(position: Int) {
        if (blockCenter && position == 4)
            return
        stage.addActor(GetEntityOrIdentityView { addRuleData(position, it) })
    }

    private fun addRuleData(position: Int, data: Triple<GetEntityOrIdentityView.EntityOrIdentity, String, Boolean>) {
        if (data.first == GetEntityOrIdentityView.EntityOrIdentity.IDENTITY) {
            rulesButton.setTexture(position, Textures.get(data.second.lowercase() + "Identity"))
            rulesButton.setNot(position, data.third)
            rules[position] = NameOrIdentity(
                Identity::class.nestedClasses.find { it.simpleName == data.second }!! as KClass<out Identity>,
                data.third
            )
            return
        }
        val entity = Entities.new(data.second)
        SingleEntityDrawer(entity)
        rulesButton.setTexture(position, entity.get(TextureAttribute::class)!!.textures.maxBy { it.width() + it.height() })
        rulesButton.setNot(position, data.third)
        rules[position] = NameOrIdentity(data.second, data.third)
    }

    fun getRules(): Pair<List<NameOrIdentity?>, RulePickerButton> {
        return rules to rulesButton
    }
}













