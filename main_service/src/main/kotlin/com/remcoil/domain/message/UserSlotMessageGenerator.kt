package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.OperationSaver

class UserSlotOpenMessageGenerator(saver: OperationSaver) :
    MessageGenerator<OperationWithData.UserSlotOpen>(saver) {
    override fun isCorrectOperation(operation: OperationWithData): Boolean =
        operation is OperationWithData.UserSlotOpen

    override fun generate(operation: OperationWithData.UserSlotOpen): String {
        val user = operation.user
        val slot = operation.slot
        return generateMessage(
            entity = "пользователь ${user.firstname} ${user.lastname}",
            action = "открыл ячейку №${slot.number}"
        )
    }
}

class UserSlotUpdateMessageGenerator(saver: OperationSaver) :
    MessageGenerator<OperationWithData.UserSlotUpdate>(saver) {
    override fun isCorrectOperation(operation: OperationWithData): Boolean =
        operation is OperationWithData.UserSlotUpdate

    override fun generate(operation: OperationWithData.UserSlotUpdate): String {
        val user = operation.user
        val slot = operation.slot
        val task = operation.task
        return generateMessage(
            entity = "пользователь ${user.firstname} ${user.lastname}",
            action = "изменил задачу в ячейке №${slot.number} на ${task?.qrCode ?: "ничего"}"
        )
    }
}