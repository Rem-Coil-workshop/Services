package com.remcoil.gateway.service.history

import com.remcoil.domain.files.DirectoryHelper
import com.remcoil.domain.files.OperationHistorySaver
import com.remcoil.domain.log.MessageGenerator
import com.remcoil.utils.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OperationsHistoryService(
    private val directory: DirectoryHelper,
    private val messageGenerator: MessageGenerator,
    private val operationSaver: OperationHistorySaver
) {
    suspend fun getAllFiles(): List<String> = withContext(Dispatchers.IO) {
        val files = directory.getAllFiles()
        logger.info("Отдали весь список файлов логов операций с ящиками")
        files
    }

    suspend fun save(qrCode: String, cardCode: Int) = withContext(Dispatchers.IO) {
        val message = messageGenerator.generate(qrCode, cardCode)
        operationSaver.save(message)
        logger.info("Создали лог с значениями: qr code = $qrCode, card = $cardCode")
    }
}