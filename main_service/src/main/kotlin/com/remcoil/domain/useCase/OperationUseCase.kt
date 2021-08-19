package com.remcoil.domain.useCase

import com.remcoil.data.exception.slot.NoSuchSlotException
import com.remcoil.data.model.operation.Operation
import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.message.MessageUseCase
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val currentThreadContext = newSingleThreadContext("OPERATIONS")

    suspend fun saveOperation(operation: Operation) = CoroutineScope(currentThreadContext).launch {
        val operationWithData = when (operation) {
            is Operation.EmployeeOpenedSlot -> mapOperation(operation)
        }
        messageUseCase.saveOperation(operationWithData)
    }

    private suspend fun mapOperation(operation: Operation.EmployeeOpenedSlot): OperationWithData = coroutineScope {
        val qrCode = operation.qrCode
        val employee = async {
            employeeUseCase.getByNumber(operation.card)
        }
        val slot = async {
            val task = taskUseCase.getByQrCode(qrCode)!!
            return@async slotUseCase.getByTask(task)
                ?: throw NoSuchSlotException("Ячейки с такой задачей не существует")
        }
        return@coroutineScope OperationWithData.EmployeeOpenedSlot(employee.await(), slot.await(), qrCode)
    }
}