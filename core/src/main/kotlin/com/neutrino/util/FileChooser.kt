package com.neutrino.util

import com.badlogic.gdx.files.FileHandle
import com.neutrino.util.Constants.fileChooser
import java.awt.FileDialog
import java.awt.Frame

class FileChooser {
    private val fileDialog = FileDialog(null as Frame?)

    init {
        fileDialog.isMultipleMode = true
        fileDialog.file = "*.jpg;*.jpeg;*.png"
    }

    fun getFile(callback: FileChooserCallback) {
        fileChooser.chooseFile(callback)
    }

    private fun chooseFile(callback: FileChooserCallback) {
        fileDialog.isVisible = true
        val files = fileDialog.files
        if (files == null || files.isEmpty()) {
            callback.cancelled()
        } else {
            callback.fileChosen(files.map { FileHandle(it) })
        }
    }
}
