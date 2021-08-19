package com.remcoil.domain.validator

import com.remcoil.data.exception.slot.SlotOpenException
import com.remcoil.domain.device.SlotState
import com.remcoil.domain.useCase.EmployeeUseCase
import com.remcoil.domain.useCase.PermissionUseCase
import com.remcoil.domain.useCase.TaskUseCase

class EmployeeDataValidator(
    private val taskUseCase: TaskUseCase,
    private val employeeUseCase: EmployeeUseCase,
    private val permissionUseCase: PermissionUseCase
) {
    suspend fun validateCard(card: Int) {
        employeeUseCase.checkByCard(card)
    }

    suspend fun validateQr(qrCode: String) {
        if (taskUseCase.whichSlot(qrCode) == null)
            throw SlotOpenException("Данной задачи не существует или она не лежит в какой либо ячейке")
    }

    suspend fun checkPermission(state: SlotState): Boolean {
        val employee = employeeUseCase.getByNumber(state.cardNumber)
        val permittedTasks = permissionUseCase.getPermittedTasks(employee.id!!)
        return permittedTasks.map { task -> task.qrCode }.contains(state.qrCode)
    }
}