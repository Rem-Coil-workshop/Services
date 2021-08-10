package com.remcoil.domain.useCase.validator

import com.remcoil.domain.service.box.BoxesService
import com.remcoil.domain.service.employee.EmployeesService
import com.remcoil.domain.service.employee.PermissionsService

class SlotValidatorImpl(
    private val employeesService: EmployeesService,
    private val boxesService: BoxesService,
    private val permissionsService: PermissionsService
) : SlotValidator {
    override suspend fun validateCard(card: Int) {
        employeesService.checkByCard(card)
    }

    override suspend fun validateQr(qrCode: String) {
        boxesService.getByQrCode(qrCode)
    }

    override suspend fun checkPermission(card: Int, qrCode: String): Boolean {
        val employee = employeesService.getByEmployeeNumber(card)
        val permittedTasks = permissionsService.getPermittedTasks(employee.id!!)
        return permittedTasks.map { task -> task.qrCode }.contains(qrCode)
    }
}