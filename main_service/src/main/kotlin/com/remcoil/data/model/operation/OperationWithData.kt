package com.remcoil.data.model.operation

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.slot.Slot
import com.remcoil.data.model.task.Task
import com.remcoil.data.model.user.User

sealed interface OperationWithData {
    class EmployeeOpenedSlot(val employee: Employee, val slot: Slot, val task: String) : OperationWithData
    class UserSlotOpen(val user: User, val slot: Slot) : OperationWithData
    class UserSlotUpdate(val user: User, val slot: Slot, val task: Task?) : OperationWithData
    class UserChangePermission(val user: User, val employee: Employee, val task: Task, val isAdd: Boolean) :
        OperationWithData
}