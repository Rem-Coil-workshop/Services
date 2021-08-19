package com.remcoil.domain.useCase

import com.remcoil.data.exception.employee.NoPermissionForTaskException
import com.remcoil.domain.device.SlotStateEntity
import com.remcoil.domain.validator.EmployeeDataValidator

class SlotStateUseCase(
    private val validator: EmployeeDataValidator,
    private val slotStateEntity: SlotStateEntity,
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
                // Сохраняем операцию operationsService.onSlotOpened(state.qrCode, state.cardNumber)
            } else {
                slotStateEntity.reset()
                throw NoPermissionForTaskException("No permission for this task")
            }
        }
    }

    suspend fun resetState() {
        slotStateEntity.reset()
    }
}