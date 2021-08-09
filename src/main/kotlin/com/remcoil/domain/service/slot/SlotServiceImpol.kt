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
            if (opener.openByQrCode(state.qrCode)) {
                logsService.log(state.qrCode, state.cardNumber)
            }
            state.reset()
        }
    }

    override suspend fun resetState() {
        state.reset()
    }
}
