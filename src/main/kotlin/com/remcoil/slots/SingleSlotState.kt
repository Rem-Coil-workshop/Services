package com.remcoil.slots

import com.remcoil.utils.logger

class SingleSlotState(
    private val led: LedHelper
) : SlotState {
    companion object {
        const val DEFAULT_CARD_VALUE = -1
        const val DEFAULT_QR_CODE_VALUE = ""
    }

    override var cardNumber: Int = DEFAULT_CARD_VALUE
        private set(card) {
            if (field == DEFAULT_CARD_VALUE || card == DEFAULT_CARD_VALUE) {
                    field = card
                    logger.info("Установлено значение карточки: $card")
            }
        }

    override var qrCode: String = DEFAULT_QR_CODE_VALUE
        private set(code) {
            if (field == DEFAULT_QR_CODE_VALUE || code == DEFAULT_QR_CODE_VALUE) {
                field = code
                logger.info("Установлено значение qr кода: $code")
            }
        }

    override val isReady
        get() =
            qrCode != DEFAULT_QR_CODE_VALUE && cardNumber != DEFAULT_CARD_VALUE

    override suspend fun setCardNumber(card: Int) {
        cardNumber = card
        led.turnCard()
    }

    override suspend fun setQrNumber(qr: String) {
        qrCode = qr
        led.turnQr()
    }

    override suspend fun reset() {
        qrCode = DEFAULT_QR_CODE_VALUE
        cardNumber = DEFAULT_CARD_VALUE
        led.turnOff()
        logger.info("Сброс значений")
    }
}