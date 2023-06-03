package attributes

import com.neutrino.entities.Entity

abstract class Attribute {

    private var _entity: Entity? = null

    var entity: Entity
        set(value) {_entity = value}
        get() = _entity!!

    open fun onEntityAttached() {}

//    fun <T: attributes.Attribute> getThisAttribute(attributeClass: KClass<T>): T {
//        return Entities[entity.id]!!.getAttribute(this::class)!! as T
//    }
}
