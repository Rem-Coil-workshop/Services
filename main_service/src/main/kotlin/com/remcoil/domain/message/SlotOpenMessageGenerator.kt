package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.OperationSaver

class SlotOpenMessageGenerator(saver: OperationSaver) : MessageGenerator<OperationWithData.EmployeeOpenedSlot>(saver) {
    override fun isCorrectOperation(operation: OperationWithData): Boolean =
        operation is OperationWithData.EmployeeOpenedSlot

    override fun generate(operation: OperationWithData.EmployeeOpenedSlot): String {
        val employee = operation.employee
        return generateMessage(
            entity = "рабочий ${employee.surname} ${employee.name}",
            action = "взял материалы для задачи ${operation.task} в ячейке ${operation.slot.number}"
        )
    }
}