package com.neutrino.textures

import com.neutrino.util.addInitial

class LightSources() {

    var isSingleLight = false

    private var light : Light? = null
    private var lights: ArrayList<ArrayList<Light>?>? = null

    constructor(light: Light): this() {
        this.light = light
        isSingleLight = true
    }

    constructor(lights: ArrayList<Light>) : this() {
        this.lights = ArrayList(1)
        this.lights!!.add(lights)
    }

    constructor(lights: List<ArrayList<Light>?>) : this() {
        this.lights = ArrayList(lights)
    }

    fun add(lights: ArrayList<Light>?) {
        if (this.lights == null)
            this.lights = ArrayList(1)
        this.lights!!.add(lights)
    }

    fun getLight(): Light {
        return light!!
    }

    fun getLights(i: Int = 0): ArrayList<Light>? {
        return lights?.get(i) ?: if (light != null) ArrayList<Light>().addInitial(light!!) else null
    }

    override fun toString(): String {
        return "LightSources(" +
            if (isSingleLight) getLight().toString() + ")"
            else arrayToString() + ")"
    }

    private fun arrayToString(): String {
        val builder = StringBuilder(300)
        fun addStringArray(i: Int, tabs: String) {
            val array = getLights(i)
            if (array == null) {
                builder.append("null,")
                return
            }
            builder.append("arrayListOf(")
            for (light in array) {
                builder.append("\n\t${tabs}$light,")
            }
            if (array.isNotEmpty())
                builder.append("\n$tabs")
            builder.append("),")
        }
        var tabs = ""
        if (lights!!.size != 1) {
            builder.append("listOf(")
            tabs = "\t"
        }
        for (i in 0 until lights!!.size) {
            addStringArray(i, tabs)
        }
        if (lights!!.size != 1)
            builder.append(")")

        return builder.toString()
    }

}
