package com.remcoil.domain.files

import com.remcoil.utils.logged

abstract class DirectoryViewer(private val messageOnView: String) {
    protected abstract suspend fun getAllFiles(): List<String>

    suspend fun view():List<String> = logged(messageOnView) {
        getAllFiles()
    }
}