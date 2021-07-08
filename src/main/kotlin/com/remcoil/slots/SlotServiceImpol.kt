package com.remcoil.slots

import com.remcoil.employees.EmployeesService
import com.remcoil.logs.LogsService

class SlotServiceImpl(
    private val employeesService: EmployeesService,
    private val logsService: LogsService,
    private val opener: SlotOpener,
    private val state: SlotState
) : SlotService {
    override suspend fun setCardNumber(card: Int) {
        employeesService.checkByCard(card)
        state.setCardNumber(card)
        openSlot()
    }

    override suspend fun setQrCode(qrCode: String): Boolean {
        val isCodeValid = true // TODO: 08.07.2021 Добавить логику проверки валиндности кода
        if (isCodeValid) {
            state.setQrNumber(qrCode)
            openSlot()
        }
        return isCodeValid
    }

    private suspend fun openSlot() {
        if (state.isReady) {
            if (opener.open(state.qrCode)) {
                logsService.log(state.qrCode, state.cardNumber)
            }
            state.reset()
        }
    }

    override suspend fun resetState() {
        state.reset()
    }
}
