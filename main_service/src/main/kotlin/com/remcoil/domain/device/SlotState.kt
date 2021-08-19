package com.remcoil.domain.device

data class SlotState(
    val cardNumber: Int = DEFAULT_CARD_VALUE,
    val qrCode: String = DEFAULT_QR_CODE_VALUE
) {
    companion object {
        const val DEFAULT_CARD_VALUE = -1
        const val DEFAULT_QR_CODE_VALUE = ""
    }

    val isReady get() = qrCode != DEFAULT_QR_CODE_VALUE && cardNumber != DEFAULT_CARD_VALUE
}