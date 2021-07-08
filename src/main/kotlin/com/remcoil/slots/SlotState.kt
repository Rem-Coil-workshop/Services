package com.remcoil.slots

interface SlotState {
    var cardNumber: Int

    var qrCode: String

    val isReady: Boolean

    fun reset()
}