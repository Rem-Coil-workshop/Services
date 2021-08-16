package com.remcoil.gateway.controller.slot

import com.remcoil.gateway.service.slot.SlotStateService
import com.remcoil.utils.logger
import java.util.*

class SlotController(private val slotStateService: SlotStateService) {
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
            slotStateService.onCardNumberEntered(card)
        }
    }

    suspend fun setQr(qr: String) {
        slotStateService.onQrCodeEntered(qr)
    }

    suspend fun reset() {
        if (modules.isNotEmpty()) {
            closeAllConnection()
            logger.info("Сброс всех соединений")
        } else {
            slotStateService.resetState()
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