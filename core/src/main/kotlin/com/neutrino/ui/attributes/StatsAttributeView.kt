package com.neutrino.ui.attributes

import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.kotcrab.vis.ui.Sizes
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.building.utilities.Alignment
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextField
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel
import com.kotcrab.vis.ui.widget.spinner.Spinner
import com.neutrino.entities.util.RangeType
import com.neutrino.ui.LeftTable
import ktx.actors.onChange
import ktx.actors.onClick
import ktx.scene2d.scene2d
import ktx.scene2d.vis.menuItem
import ktx.scene2d.vis.separator
import java.lang.reflect.Field

class StatsAttributeView: AttributeView(VisTable()) {

    override val attributeName: String = "Stats"
    override fun generateString(): String {
        val builder = StringBuilder(50)
        fun addStat(stat: Field) {
            builder.append("\t${stat.name} = ")
            if (stat.name == "rangeType")
                builder.append("RangeType.")

            val statValue = stat.get(stats)
            builder.append(when (stat.type.toString()) {
                "float" -> {
                    val i = (statValue as Float).toInt()
                    val valueString = if (statValue.compareTo(i) == 0) i.toString() else statValue.toString()
                    "${valueString}f,\n"
                }
                "double" -> "$statValue,\n"
                "int" -> "${statValue as Int},\n"
                else -> "$statValue,\n"
            })
        }
        builder.append("Stats(\n")
        val defaultStats = Stats()
        for (stat in Stats::class.java.declaredFields) {
            stat.trySetAccessible()
            if (stat.get(defaultStats) != stat.get(stats))
                addStat(stat)
        }
        if (builder.length == 16)
            builder.deleteAt(builder.length - 1)
        builder.append(")")
        return builder.toString()
    }

    private val stats = Stats()

    init {
        TableUtils.setSpacingDefaults(table)
        table.padTop(16f)
        table.padBottom(16f)
        table.addStatPicker("HpMax", stats.hpMax, false)
            .also { it.actor.onChange { stats.hpMax = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("MpMax", stats.mpMax, false)
            .also { it.actor.onChange { stats.mpMax = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Strength", stats.strength, true)
            .also { it.actor.onChange { stats.strength = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Dexterity", stats.dexterity, true)
            .also { it.actor.onChange { stats.dexterity = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Intelligence", stats.intelligence, true)
            .also { it.actor.onChange { stats.intelligence = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Luck", stats.luck, true)
            .also { it.actor.onChange { stats.luck = it.actor.textField.text.toFloat() } }.row()
        table.add(scene2d.separator()).growX().padLeft(16f).padRight(16f).row()
        table.addStatPicker("Damage", stats.damage, true)
            .also { it.actor.onChange { stats.damage = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("DamageVariation", stats.damageVariation, true)
            .also { it.actor.onChange { stats.damageVariation = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Defence", stats.defence, true)
            .also { it.actor.onChange { stats.defence = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Range", stats.range.toFloat(), false)
            .also { it.actor.onChange { stats.range = it.actor.textField.text.toInt() } }.row()
        table.addRangeType()
        table.add(scene2d.separator()).growX().padLeft(16f).padRight(16f).row()
        table.addStatPicker("CriticalChance", stats.criticalChance, true, "0.1")
            .also { it.actor.onChange { stats.criticalChance = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("CriticalDamage", stats.criticalDamage, true, "0.1")
            .also { it.actor.onChange { stats.criticalDamage = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("AttackSpeed", stats.attackSpeed.toFloat(), true, "0.1")
            .also { it.actor.onChange { stats.attackSpeed = it.actor.textField.text.toDouble() } }.row()
        table.addStatPicker("MovementSpeed", stats.movementSpeed.toFloat(), true, "0.1")
            .also { it.actor.onChange { stats.movementSpeed = it.actor.textField.text.toDouble() } }.row()
        table.add(scene2d.separator()).growX().padLeft(16f).padRight(16f).row()
        table.addStatPicker("Evasion", stats.evasion, true, "0.1")
            .also { it.actor.onChange { stats.evasion = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Accuracy", stats.accuracy, true, "0.1")
            .also { it.actor.onChange { stats.accuracy = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("Stealth", stats.stealth, true, "0.1")
            .also { it.actor.onChange { stats.stealth = it.actor.textField.text.toFloat() } }.row()
        table.add(scene2d.separator()).growX().padLeft(16f).padRight(16f).row()
        table.addStatPicker("FireDamage", stats.fireDamage, true)
            .also { it.actor.onChange { stats.fireDamage = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("WaterDamage", stats.waterDamage, true)
            .also { it.actor.onChange { stats.waterDamage = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("AirDamage", stats.airDamage, true)
            .also { it.actor.onChange { stats.airDamage = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("PoisonDamage", stats.poisonDamage, true)
            .also { it.actor.onChange { stats.poisonDamage = it.actor.textField.text.toFloat() } }.row()
        table.add(scene2d.separator()).growX().padLeft(16f).padRight(16f).row()
        table.addStatPicker("FireDefence", stats.fireDefence, true)
            .also { it.actor.onChange { stats.fireDefence = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("WaterDefence", stats.waterDefence, true)
            .also { it.actor.onChange { stats.waterDefence = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("AirDefence", stats.airDefence, true)
            .also { it.actor.onChange { stats.airDefence = it.actor.textField.text.toFloat() } }.row()
        table.addStatPicker("PoisonDefence", stats.poisonDefence, true)
            .also { it.actor.onChange { stats.poisonDefence = it.actor.textField.text.toFloat() } }.row()
    }

    private fun VisTable.addRangeType() {
        add(VisLabel("RangeType: ", Alignment.LEFT.alignment))
            .padLeft(16f).width(LeftTable.WIDTH / 2).expandX()
        val rangeTypeTable = VisTable()
        val rangeTypeLabel = VisLabel(stats.rangeType.name)
        rangeTypeTable.background = VisTextField("").style.background
        rangeTypeTable.add(rangeTypeLabel).expandX()
        rangeTypeLabel.onClick { getRangeTypeMenu(rangeTypeLabel) }
        add(rangeTypeTable).row()
    }

    private fun getRangeTypeMenu(label: VisLabel) {
        val menu = PopupMenu()
        RangeType.values().forEach { type ->
            menu.menuItem(type.name).onClick {
                label.setText(type.name)
                stats.rangeType = type
            }
        }
        menu.showMenu(stage, label)
    }

    private fun VisTable.addStatPicker(name: String, value: Float, decimal: Boolean, step: String = "1.0"): Cell<Spinner> {
        add(VisLabel("$name: ", Alignment.LEFT.alignment))
            .padLeft(16f).width(LeftTable.WIDTH / 2).expandX()
        val sizes = Sizes(VisUI.getSizes())
        sizes.spinnerFieldSize = 64f
        sizes.spinnerButtonHeight -= 4f
        val txt = Spinner(
            VisUI.getSkin()["default", Spinner.SpinnerStyle::class.java], sizes,"",
            if (decimal)
                FloatSpinnerModel(value.toBigDecimal().toString(), "0.0", "999.0", step)
            else
                IntSpinnerModel(value.toInt(), 0, 999, 1)
        )
        txt.name = name
        return add(txt).left()
    }

    private data class Stats(
        var hpMax: Float = 0f,
        var hp: Float = 0f,
        var mpMax: Float = 0f,
        var mp: Float = 0f,
        var strength: Float = 0f,
        var dexterity: Float = 0f,
        var intelligence: Float = 0f,
        var luck: Float = 0f,
        var damage: Float = 0f,
        var damageVariation: Float = 0f,
        var defence: Float = 0f,
        var evasion: Float = 0f,
        var accuracy: Float = 1f,
        var criticalChance: Float = 0f,
        var criticalDamage: Float = 1f,
        var attackSpeed: Double = 1.0,
        var movementSpeed: Double = 1.0,
        var range: Int = 1,
        var rangeType: RangeType = RangeType.SQUARE,
        var experience: Float = 0f,
        var stealth: Float = 0f,
        // elemental
        var fireDamage: Float = 0f,
        var waterDamage: Float = 0f,
        var earthDamage: Float = 0f,
        var airDamage: Float = 0f,
        var poisonDamage: Float = 0f,
        var fireDefence: Float = 0f,
        var waterDefence: Float = 0f,
        var airDefence: Float = 0f,
        var poisonDefence: Float = 0f
    )
}
