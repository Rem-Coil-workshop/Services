package com.remcoil.domain.service.slot

interface SlotService {
    suspend fun onCardNumberEntered(card: Int)

    suspend fun onQrCodeEntered(qrCode: String)

    suspend fun resetState()
}
