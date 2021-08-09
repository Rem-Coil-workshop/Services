package com.remcoil.domain.useCase.validator

import com.remcoil.domain.service.box.BoxesService
import com.remcoil.domain.service.employee.EmployeesService

class SlotValidatorImpl(
    private val employeesService: EmployeesService,
    private val boxesService: BoxesService,
) : SlotValidator {
    override suspend fun validateCard(card: Int) {
        employeesService.checkByCard(card)
    }

    override suspend fun validateQr(qrCode: String) {
        boxesService.getByQrCode(qrCode)
    }
}