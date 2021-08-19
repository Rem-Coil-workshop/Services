package com.remcoil.domain.useCase

import com.remcoil.data.exception.employee.NoPermissionForTaskException
import com.remcoil.data.model.operation.Operation
import com.remcoil.domain.device.SlotStateEntity
import com.remcoil.domain.validator.EmployeeDataValidator

class SlotStateUseCase(
    private val validator: EmployeeDataValidator,
    private val slotStateEntity: SlotStateEntity,
    private val operationUseCase: OperationUseCase,
) {
    suspend fun onCardNumberEntered(card: Int) {
        validator.validateCard(card)
        slotStateEntity.setCardNumber(card)
        tryOpenSlot()
    }

    suspend fun onQrCodeEntered(qrCode: String) {
        validator.validateQr(qrCode)
        slotStateEntity.setQrNumber(qrCode)
        tryOpenSlot()
    }

    private suspend fun tryOpenSlot() {
        if (slotStateEntity.stateIsReady()) {
            val isPermitted = validator.checkPermission(slotStateEntity.state)
            if (isPermitted) {
                slotStateEntity.open()
                saveOperation()
                resetState()
            } else {
                resetState()
                throw NoPermissionForTaskException("No permission for this task")
            }
        }
    }

    private suspend fun saveOperation() {
        val state = slotStateEntity.state
        val operation = Operation.EmployeeOpenedSlot(qrCode = state.qrCode, card = state.cardNumber)
        operationUseCase.saveOperation(operation)
    }

    suspend fun resetState() {
        slotStateEntity.reset()
    }
}