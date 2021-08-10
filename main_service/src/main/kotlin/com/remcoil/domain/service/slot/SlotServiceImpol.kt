package com.remcoil.domain.service.slot

import com.remcoil.domain.service.log.JobLogsService
import com.remcoil.domain.useCase.validator.SlotValidator
import com.remcoil.presentation.device.SlotOpener
import com.remcoil.presentation.device.SlotState

class SlotServiceImpl(
    private val validator: SlotValidator,
    private val logsService: JobLogsService,
    private val opener: SlotOpener,
    private val state: SlotState
) : SlotService {
    override suspend fun onCardNumberEntered(card: Int) {
        validator.validateCard(card)
        state.setCardNumber(card)
        openSlot()
    }

    override suspend fun onQrCodeEntered(qrCode: String) {
        validator.validateQr(qrCode)
        state.setQrNumber(qrCode)
        openSlot()
    }

    private suspend fun openSlot() {
        if (state.isReady) {
            if (isOpen()) logsService.log(state.qrCode, state.cardNumber)
            state.reset()
        }
    }

    private suspend fun isOpen(): Boolean {
        val isPermitted = validator.checkPermission(state.cardNumber, state.qrCode)
        if (!isPermitted) {
            state.reset()
            throw Exception("No permission for this task")
        }
        return opener.openByQrCode(state.qrCode)
    }

    override suspend fun resetState() {
        state.reset()
    }
}
