package com.neutrino.util

import com.neutrino.ui.UiManager

object UiManagerFactory {

    private var uiManager: UiManager? = null
    private var uiCallQueue: ArrayList<(UiManager) -> Unit> = ArrayList()

    fun registerUiManager(uiManager: UiManager) {
        this.uiManager = uiManager
        uiCallQueue.forEach { it.invoke(uiManager) }
        uiCallQueue.clear()
    }

    fun getUI(): UiManager {
        return uiManager!!
    }

    fun addCallToQueue(uiCall: (UiManager) -> Unit) {
        uiCallQueue.add(uiCall)
    }
}
