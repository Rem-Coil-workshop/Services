package com.remcoil.domain.useCase

interface SlotValidator {
    suspend fun validateCard(card: Int)

    suspend fun validateQr(qrCode: String)
}