package com.neutrino.util

import com.neutrino.ui.UiManager

object UiManagerFactory {

    private var uiManager: UiManager? = null

    fun registerUiManager(uiManager: UiManager) {
        this.uiManager = uiManager
    }

    fun getUI(): UiManager {
        return uiManager!!
    }
}
