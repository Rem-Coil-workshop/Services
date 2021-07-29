package com.remcoil.domain.service.log

import com.remcoil.domain.useCase.files.DirectoryHelper
import com.remcoil.domain.useCase.files.OperationLogger
import com.remcoil.domain.useCase.log.LogMessageGenerator
import com.remcoil.utils.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogsService(
    private val directory: DirectoryHelper,
    private val messageGenerator: LogMessageGenerator,
    private val operationLogger: OperationLogger
) {

    suspend fun getAllFiles(): List<String> = withContext(Dispatchers.IO) {
        val files = directory.getAllFiles()
        logger.info("Отдали весь список файлов логов")
        files
    }

    suspend fun log(qrCode: String, cardCode: Int) = withContext(Dispatchers.IO) {
        val message = messageGenerator.generate(qrCode, cardCode)
        operationLogger.log(message)
        logger.info("Создали лог с значениями: qr code = $qrCode, card = $cardCode")
    }
}