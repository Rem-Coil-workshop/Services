package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.OperationSaver

class UserPermissionMessageGenerator(saver: OperationSaver) :
    MessageGenerator<OperationWithData.UserChangePermission>(saver) {

    override fun isCorrectOperation(operation: OperationWithData): Boolean =
        operation is OperationWithData.UserChangePermission

    override fun generate(operation: OperationWithData.UserChangePermission): String {
        val user = operation.user
        val employee = operation.employee
        val task = operation.task
        val isAdd = if (operation.isAdd) "добавил" else "забрал"
        return generateMessage(
            entity = "пользователь ${user.firstname} ${user.lastname} ",
            action = "$isAdd разрешение для ${employee.fullName} на задачу ${task.qrCode}"
        )
    }
}