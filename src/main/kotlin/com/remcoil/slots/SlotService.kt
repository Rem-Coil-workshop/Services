package com.remcoil.slots

interface SlotService {
    fun setCardNumber(card: Int)

    fun setQrCode(qrCode: String): Boolean

    fun resetState()
}
