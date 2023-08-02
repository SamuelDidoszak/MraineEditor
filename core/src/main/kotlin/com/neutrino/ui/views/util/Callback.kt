package com.neutrino.ui.views.util

interface Callback<T> {
    val callback: (data: T) -> Unit
}
