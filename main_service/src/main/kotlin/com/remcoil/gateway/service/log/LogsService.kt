package com.remcoil.gateway.service.log

import com.remcoil.domain.files.DirectoryHelper
import com.remcoil.utils.logged

class LogsService(private val directory: DirectoryHelper) {
    suspend fun getAllLogFiles(): List<String> = logged("Отдали весь список файлов логов системы") {
        return directory.getAllFiles()
    }
}