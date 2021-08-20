package com.remcoil.controller.slot

import com.remcoil.domain.useCase.SlotStateUseCase
import com.remcoil.utils.logger
import java.util.*

class SlotStateController(private val stateUseCase: SlotStateUseCase) {
    interface CardObserver {
        suspend fun onCardEntered(card: Int)

        suspend fun onClose()
    }

    private val modules = Collections.synchronizedList(mutableListOf<CardObserver>())

    fun subscribe(module: CardObserver) {
        modules.add(module)
    }

    fun unsubscribe(module: CardObserver) {
        modules.remove(module)
    }

    suspend fun setCard(card: Int) {
        if (modules.isNotEmpty()) {
            notify(card)
        } else {
            stateUseCase.onCardNumberEntered(card)
        }
    }

    suspend fun setQr(qr: String) {
        stateUseCase.onQrCodeEntered(qr)
    }

    suspend fun reset() {
        if (modules.isNotEmpty()) {
            closeAllConnection()
            logger.info("Сброс всех соединений")
        } else {
            stateUseCase.resetState()
        }
    }

    private suspend fun notify(card: Int) {
        modules.forEach { module ->
            module.onCardEntered(card)
        }
    }

    private suspend fun closeAllConnection() {
        modules.forEach { module ->
            module.onClose()
        }
    }
}