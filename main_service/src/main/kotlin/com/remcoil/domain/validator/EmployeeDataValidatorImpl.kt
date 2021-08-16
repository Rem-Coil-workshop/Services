package com.remcoil.domain.validator

import com.remcoil.gateway.service.employee.EmployeesService
import com.remcoil.gateway.service.employee.PermissionsService
import com.remcoil.gateway.service.slot.SlotsService

class EmployeeDataValidatorImpl(
    private val slotsInteractor: SlotsService,
    private val employeesInteractor: EmployeesService,
    private val permissionsInteractor: PermissionsService
) : EmployeeDataValidator {
    override suspend fun validateCard(card: Int) {
        employeesInteractor.checkByCard(card)
    }

    override suspend fun validateQr(qrCode: String) {
        slotsInteractor.getByQrCode(qrCode)
    }

    override suspend fun checkPermission(card: Int, qrCode: String): Boolean {
        val employee = employeesInteractor.getByEmployeeNumber(card)
        val permittedTasks = permissionsInteractor.getPermittedTasks(employee.id!!)
        return permittedTasks.map { task -> task.qrCode }.contains(qrCode)
    }
}