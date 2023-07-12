package com.neutrino.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Array
import kotlin.math.abs
import kotlin.math.roundToInt

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

infix fun <T> ArrayList<T>.add(element: T?): Boolean {
    if (element == null)
        return false
    return add(element)
}

fun getChangeListener(method: (event: ChangeListener.ChangeEvent?, actor: Actor?) -> Unit): ChangeListener {
    return object : ChangeListener() {
        override fun changed(event: ChangeEvent?, actor: Actor?) {
            method.invoke(event, actor)
        }
    }
}
