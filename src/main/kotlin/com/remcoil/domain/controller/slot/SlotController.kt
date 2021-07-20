package com.remcoil.domain.controller.slot

import com.remcoil.domain.service.slot.SlotService
import java.util.*

class SlotController(
    private val slotService: SlotService,
) {
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
            slotService.onCardNumberEntered(card)
        }
    }

    suspend fun setQr(qr: String) {
        slotService.onQrCodeEntered(qr)
    }

    suspend fun reset() {
        slotService.resetState()
    }

    private suspend fun notify(card: Int) {
        modules.forEach { module ->
            module.onCardEntered(card)
        }
    }

}