package com.remcoil.gateway.service.slot

import com.remcoil.domain.device.SlotOpener
import com.remcoil.domain.device.SlotState
import com.remcoil.domain.validator.EmployeeDataValidator
import com.remcoil.gateway.service.history.OperationsHistoryService

class SlotStateServiceImpl(
    private val validator: EmployeeDataValidator,
    private val logsService: OperationsHistoryService,
    private val opener: SlotOpener,
    private val state: SlotState
) : SlotStateService {
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
            if (isOpen()) logsService.save(state.qrCode, state.cardNumber)
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
