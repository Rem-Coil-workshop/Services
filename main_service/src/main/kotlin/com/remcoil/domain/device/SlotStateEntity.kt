package com.remcoil.domain.device

import com.remcoil.domain.device.SlotState.Companion.DEFAULT_CARD_VALUE
import com.remcoil.domain.device.SlotState.Companion.DEFAULT_QR_CODE_VALUE
import com.remcoil.domain.useCase.TaskUseCase
import com.remcoil.utils.logger

class SlotStateEntity(
    private val ledService: LedService,
    private val taskUseCase: TaskUseCase,
    private val slotOpener: SlotOpener,
) {
    var state = SlotState()
        private set

    fun stateIsReady() = state.isReady

    suspend fun setCardNumber(card: Int) {
        if (state.cardNumber == DEFAULT_CARD_VALUE || card == DEFAULT_CARD_VALUE) {
            logger.info("Установлено значение карточки: $card")
            state = state.copy(cardNumber = card)
            ledService.turnCard()
        }
    }

    suspend fun setQrNumber(code: String) {
        if (state.qrCode == DEFAULT_QR_CODE_VALUE || code == DEFAULT_QR_CODE_VALUE) {
            state = state.copy(qrCode = code)
            ledService.turnQr()
            logger.info("Установлено значение qr кода: $code")
        }
    }


    suspend fun open() {
        val slot = taskUseCase.whichSlot(state.qrCode)!!
        slotOpener.open(slot.number)
    }

    suspend fun reset() {
        logger.info("Сброс значений")
        state = SlotState()
        ledService.turnOff()
    }
}