package com.remcoil.gateway.service.history

import com.remcoil.domain.files.OperationHistorySaver
import com.remcoil.domain.message.MessageGenerator
import com.remcoil.utils.logged

class SlotOperationsHistoryService(
    private val messageGenerator: MessageGenerator,
    private val operationSaver: OperationHistorySaver
) {
    fun onSlotOpened(qrCode: String, cardCode: Int) =
        logged("Создали лог с значениями: qr code = $qrCode, card = $cardCode") {
//            val message = messageGenerator.generate(qrCode, cardCode)
//            operationSaver.save(message)
        }
}