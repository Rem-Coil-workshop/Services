package com.remcoil.data.model.operation

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.slot.Slot

sealed interface OperationWithData {
    class EmployeeOpenedSlot(val employee: Employee, val slot: Slot, val task: String) : OperationWithData
}