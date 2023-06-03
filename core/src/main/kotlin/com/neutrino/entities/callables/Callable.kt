package callables

import com.neutrino.entities.Entity


interface Callable {
    fun call(entity: Entity)

    fun call(entity: Entity, vararg data: Any?, callOthers: Boolean = false) {
        if (callOthers)
            call(entity)
    }
}
