package com.remcoil.presentation.device

interface SlotState {
    val isReady: Boolean

    val cardNumber: Int

    val qrCode: String

    suspend fun setCardNumber(card: Int)

    suspend fun setQrNumber(qr: String)

    suspend fun reset()
}