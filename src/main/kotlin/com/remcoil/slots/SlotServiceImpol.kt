package com.remcoil.slots

import com.remcoil.employees.EmployeesService
import com.remcoil.logs.LogsService

class SlotServiceImpl(
    private val employeesService: EmployeesService,
    private val logsService: LogsService,
    private val opener: SlotOpener,
    private val state: SlotState,
) : SlotService {
    override fun setCardNumber(card: Int): Boolean {
        val isEmployeeExist = employeesService.checkByCard(card)
        if (isEmployeeExist) {
            state.cardNumber = card
            openSlot()
        }
        return isEmployeeExist
    }

    override fun setQrCode(qrCode: String): Boolean {
        val isCodeValid = true // TODO: 08.07.2021 Добавить логику проверки валиндности кода
        if (isCodeValid) {
            state.qrCode = qrCode
            openSlot()
        }
        return isCodeValid
    }

    private fun openSlot() {
        if (state.isReady) {
            if (opener.open(state.qrCode)) {
                logsService.log(state.qrCode, state.cardNumber)
            }
        }
    }

    override fun resetState() {
        state.reset()
    }
}
