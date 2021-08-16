package com.remcoil.gateway.service.slot

interface SlotStateService {
    suspend fun onCardNumberEntered(card: Int)

    suspend fun onQrCodeEntered(qrCode: String)

    suspend fun resetState()
}
