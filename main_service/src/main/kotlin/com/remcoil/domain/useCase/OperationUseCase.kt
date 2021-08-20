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
    permissionUseCase: PermissionUseCase,
) {
    init {
        employeeUseCase.operationUseCase = this
        taskUseCase.operationUseCase = this
        slotUseCase.operationUseCase = this
        permissionUseCase.operationUseCase = this
    }

    private val currentThreadContext = newSingleThreadContext("OPERATIONS")

    suspend fun saveOperation(operation: Operation) = CoroutineScope(currentThreadContext).launch {
        val operationWithData = when (operation) {
            is Operation.EmployeeOpenedSlot -> mapEmployeeOpen(operation)
            is Operation.UserWithSlot.Open -> mapUserOpenSlot(operation)
            is Operation.UserWithSlot.Update -> mapUserUpdateSlot(operation)
            is Operation.UserChangePermission -> mapUserChangePermission(operation)
        }
        messageUseCase.saveOperation(operationWithData)
    }

    private suspend fun mapEmployeeOpen(operation: Operation.EmployeeOpenedSlot): OperationWithData = coroutineScope {
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

    private suspend fun mapUserOpenSlot(operation: Operation.UserWithSlot.Open): OperationWithData = coroutineScope {
        val slot = slotUseCase.getById(operation.slotId)
        return@coroutineScope OperationWithData.UserSlotOpen(operation.user, slot)
    }

    private suspend fun mapUserUpdateSlot(operation: Operation.UserWithSlot.Update): OperationWithData =
        coroutineScope {
            val slot = slotUseCase.getById(operation.slotId)
            if (slot.taskId == null)
                return@coroutineScope OperationWithData.UserSlotUpdate(operation.user, slot, null)

            val task = taskUseCase.getById(slot.taskId)
            return@coroutineScope OperationWithData.UserSlotUpdate(operation.user, slot, task)
        }

    private fun mapUserChangePermission(operation: Operation.UserChangePermission): OperationWithData =
        OperationWithData.UserChangePermission(operation.user, operation.employee, operation.task, operation.isAdd)
}