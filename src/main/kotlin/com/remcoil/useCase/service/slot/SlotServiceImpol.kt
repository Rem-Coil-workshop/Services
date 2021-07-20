package com.remcoil.useCase.service.slot

import com.remcoil.useCase.service.box.BoxesService
import com.remcoil.useCase.service.employee.EmployeesService
import com.remcoil.useCase.service.log.LogsService
import com.remcoil.presentation.device.SlotOpener
import com.remcoil.presentation.device.SlotState

class SlotServiceImpl(
    private val employeesService: EmployeesService,
    private val boxesService: BoxesService,
    private val logsService: LogsService,
    private val opener: SlotOpener,
    private val state: SlotState
) : SlotService {
    override suspend fun onCardNumberEntered(card: Int) {
        employeesService.checkByCard(card)
        state.setCardNumber(card)
        openSlot()
    }

    override suspend fun onQrCodeEntered(qrCode: String): Boolean {
        val isCodeValid = boxesService.isQrCodeExist(qrCode)
        if (isCodeValid) {
            state.setQrNumber(qrCode)
            openSlot()
        }
        return isCodeValid
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
