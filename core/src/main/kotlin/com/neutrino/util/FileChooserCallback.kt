package com.neutrino.util

import com.badlogic.gdx.files.FileHandle

abstract class FileChooserCallback {
    abstract fun fileChosen(files: List<FileHandle>)

    open fun cancelled() {

    }
}
