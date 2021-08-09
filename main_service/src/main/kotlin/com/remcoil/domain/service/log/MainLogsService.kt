package com.remcoil.domain.service.log

import com.remcoil.domain.useCase.files.DirectoryHelper
import com.remcoil.utils.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainLogsService(
    private val directory: DirectoryHelper,
) {
    suspend fun getAllLogFiles(): List<String> = withContext(Dispatchers.IO) {
        val files = directory.getAllFiles()
        logger.info("Отдали весь список файлов логов системы")
        files
    }
}