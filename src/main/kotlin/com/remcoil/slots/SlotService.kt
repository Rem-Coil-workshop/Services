package com.remcoil.slots

interface SlotService {
    suspend fun onCardNumberEntered(card: Int)

    suspend fun onQrCodeEntered(qrCode: String): Boolean

    suspend fun resetState()
}
