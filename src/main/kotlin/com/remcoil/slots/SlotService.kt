package com.remcoil.slots

interface SlotService {
    fun setCardNumber(card: Int): Boolean

    fun setQrCode(qrCode: String): Boolean

    fun resetState()
}
