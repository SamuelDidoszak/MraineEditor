package com.neutrino.textures

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

    fun add(lights: ArrayList<Light>) {
        this.lights!!.add(lights)
    }

    fun getLight(): Light {
        return light!!
    }

    fun getLights(i: Int = 0): ArrayList<Light>? {
        return lights!![i]
    }

}
