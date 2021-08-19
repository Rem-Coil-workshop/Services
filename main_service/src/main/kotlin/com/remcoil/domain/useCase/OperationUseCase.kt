package com.remcoil.domain.useCase

import com.remcoil.data.model.operation.Operation
import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.message.MessageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OperationUseCase(
    private val messageUseCase: MessageUseCase,
    private val employeeUseCase: EmployeeUseCase,
    private val taskUseCase: TaskUseCase,
    private val slotUseCase: SlotUseCase,
) {
    init {
        employeeUseCase.operationUseCase = this
        taskUseCase.operationUseCase = this
        slotUseCase.operationUseCase = this
    }

    suspend fun saveOperation(operation: Operation) = withContext(Dispatchers.IO) {
        launch {
            val operationWithData = when (operation) {
                is Operation.SlotOpened -> mapOperation(operation)
            }
            messageUseCase.saveOperation(operationWithData)
        }
    }

    private suspend fun mapOperation(operation: Operation.SlotOpened): OperationWithData {
        val employee = employeeUseCase.getByNumber(operation.card)
        return OperationWithData.SlotOpened(employee, operation.qrCode)
    }
}