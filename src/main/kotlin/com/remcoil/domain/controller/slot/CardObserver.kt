package com.remcoil.domain.controller.slot

interface CardObserver {
    suspend fun onCardEntered(card: Int)

    suspend fun onClose()
}