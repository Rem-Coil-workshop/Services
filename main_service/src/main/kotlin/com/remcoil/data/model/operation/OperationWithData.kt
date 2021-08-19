package com.remcoil.data.model.operation

import com.remcoil.data.model.employee.Employee

sealed interface OperationWithData {
    class SlotOpened(val employee: Employee, val task: String) : OperationWithData
}