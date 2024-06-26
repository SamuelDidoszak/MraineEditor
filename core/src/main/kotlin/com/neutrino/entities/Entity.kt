package com.neutrino.entities

import attributes.Attribute
import callables.Callable
import kotlin.properties.Delegates
import kotlin.reflect.KClass

class Entity() {
    constructor(attributes: List<Attribute>): this() {
        for (attribute in attributes)
            addAttribute(attribute)
    }

    private var nameSet = false
    var id by Delegates.notNull<Int>()
    var name: String = ""
        get() {return if (nameSet) field else Entities.getName(id)}
        set(value) {
            nameSet = true
            field = value
        }


    private val attributes: HashMap<KClass<out Attribute>, Attribute> = HashMap()

    private var callables: ArrayList<Callable>? = null


    infix fun addAttribute(attribute: Attribute): Entity {
        attributes.put(attribute::class, attribute)
        attribute.entity = this
        attribute.onEntityAttached()
        return this
    }

    infix fun <T: Attribute> get(attributeClass: KClass<T>): T? {
        return attributes[attributeClass] as T?
    }

    infix fun has(attributeClass: KClass<out Attribute>): Boolean {
        return attributes[attributeClass] != null
    }


    infix fun call(callableClass: KClass<out Callable>) {
        for (callable in callables!!) {
            if (callableClass.java.isAssignableFrom(callable::class.java))
                callable.call(this)
        }
    }

    fun call(callableClass: KClass<out Callable>, vararg data: Any?, callOthers: Boolean = true) {
        for (callable in callables!!) {
            if (callableClass.java.isAssignableFrom(callable::class.java))
                callable.call(this, data, callOthers)
        }
    }

    infix fun attach(callable: Callable) {
        if (callables == null)
            callables = ArrayList()
        callables!!.add(callable)
    }

    infix fun detach(callable: Callable) {
        callables!!.remove(callable)
    }
}
