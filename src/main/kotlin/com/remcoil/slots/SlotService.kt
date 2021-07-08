package com.remcoil.slots

interface SlotService {
    suspend fun setCardNumber(card: Int)

    suspend fun setQrCode(qrCode: String): Boolean

    suspend fun resetState()
}
