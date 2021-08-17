package com.remcoil.gateway.service.history

import com.remcoil.domain.files.DirectoryHelper
import com.remcoil.domain.files.OperationHistorySaver
import com.remcoil.domain.message.MessageGenerator
import com.remcoil.utils.logged

class OperationsHistoryService(
    private val directory: DirectoryHelper,
    private val messageGenerator: MessageGenerator,
    private val operationSaver: OperationHistorySaver
) {
    suspend fun getAllFiles(): List<String> = logged("Отдали весь список файлов логов операций с ящиками") {
        return directory.getAllFiles()
    }

    suspend fun onSlotOpened(qrCode: String, cardCode: Int) =
        logged("Создали лог с значениями: qr code = $qrCode, card = $cardCode") {
            val message = messageGenerator.generate(qrCode, cardCode)
            operationSaver.save(message)
        }
}