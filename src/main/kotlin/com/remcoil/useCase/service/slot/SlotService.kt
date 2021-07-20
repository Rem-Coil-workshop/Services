package com.remcoil.useCase.service.slot

interface SlotService {
    suspend fun onCardNumberEntered(card: Int)

    suspend fun onQrCodeEntered(qrCode: String): Boolean

    suspend fun resetState()
}
