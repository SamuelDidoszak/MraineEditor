package com.neutrino.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Array
import com.neutrino.entities.Entities
import com.neutrino.entities.Entity
import com.neutrino.entities.attributes.Identity
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.reflect.KClass

fun Double.equalsDelta(other: Double) = abs(this - other) <= 0.005
fun Double.lessThanDelta(other: Double) = (this - other) < -0.0000001
fun Float.equalsDelta(other: Float) = abs(this - other) <= 0.005f
fun Float.lessThanDelta(other: Float) = (this - other) < -0.0000001

/** Cuts off the decimal value of a floating point number. Mostly used to fix the floating point precision issue with rendering */
fun Float.round() = this.roundToInt().toFloat()
/** Rounds the number to one decimal place */
fun Float.roundOneDecimal() = (this * 10).roundToInt() / 10f
/** Cuts off the decimal value of a floating point number. Mostly used to fix the floating point precision issue with rendering */
fun Double.round() = this.roundToInt().toFloat()
/** Rounds the number to one decimal place */
fun Double.roundOneDecimal() = (this * 10).roundToInt() / 10.0

/** Returns 0 if the values are the same. Returns -1 if the value is smaller than other and 1 if it's bigger */
fun Float.compareDelta(other: Float) = if (this.equalsDelta(other)) 0
else if (this.lessThanDelta(other)) -1 else 1
/** Returns 0 if the values are the same. Returns -1 if the value is smaller than other and 1 if it's bigger */
fun Double.compareDelta(other: Double) = if (this.equalsDelta(other)) 0
else if (this.lessThanDelta(other)) -1 else 1

fun <T> Array<T>.addInitial(element: T): Array<T> {
    add(element)
    return this
}

fun <T> ArrayList<T>.addInitial(element: T): ArrayList<T> {
    add(element)
    return this
}

infix fun <T> ArrayList<T>.add(element: T?): T? {
    if (element == null)
        return null
    add(element)
    return element
}

infix fun <T> ArrayList<T>.add(elements: List<T>?): T? {
    if (elements.isNullOrEmpty())
        return null
    addAll(elements)
    return elements.first()
}

fun getChangeListener(method: (event: ChangeListener.ChangeEvent?, actor: Actor?) -> Unit): ChangeListener {
    return object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            method.invoke(event, actor)
        }
    }
}

infix fun Entity.hasIdentity(identity: KClass<out Identity>): Boolean {
    return get(identity) != null || identity == Identity.Any::class
}

infix fun MutableList<Entity>.hasIdentity(identity: KClass<out Identity>): Boolean {
    for (i in indices) {
        if (get(i).get(identity) != null || identity == Identity.Any::class)
            return true
    }
    return false
}

fun EntityName.id(): Int = Entities.getId(this)
fun EntityId.name(): String = Entities.getName(this)
