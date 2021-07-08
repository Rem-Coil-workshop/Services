package com.remcoil.slots

class SingleSlotState : SlotState {
    companion object {
        const val DEFAULT_CARD_VALUE = -1
        const val DEFAULT_QR_CODE_VALUE = ""
    }

    override var cardNumber: Int = DEFAULT_CARD_VALUE
        set(card) {
            if (field == DEFAULT_CARD_VALUE) field = card
        }

    override var qrCode: String = DEFAULT_QR_CODE_VALUE
        set(code) {
            if (field == DEFAULT_QR_CODE_VALUE) field = code
        }

    override val isReady get() =
        qrCode != DEFAULT_QR_CODE_VALUE && cardNumber != DEFAULT_CARD_VALUE

    override fun reset() {
        qrCode = DEFAULT_QR_CODE_VALUE
        cardNumber = DEFAULT_CARD_VALUE
    }
}