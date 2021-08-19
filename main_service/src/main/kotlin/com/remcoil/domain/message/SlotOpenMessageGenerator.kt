package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.OperationSaver

class SlotOpenMessageGenerator(saver: OperationSaver) : MessageGenerator<OperationWithData.SlotOpened>(saver) {
    override fun isCorrectOperation(operation: OperationWithData): Boolean = operation is OperationWithData.SlotOpened

    override fun generate(operation: OperationWithData.SlotOpened): String {
        val employee = operation.employee
        return generateMessage(
            entity = "рабочий ${employee.surname} ${employee.name}",
            action = "взял материалы для задачи ${operation.task}"
        )
    }
}