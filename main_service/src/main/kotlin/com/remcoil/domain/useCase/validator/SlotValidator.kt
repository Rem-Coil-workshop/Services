package com.remcoil.domain.useCase.validator

interface SlotValidator {
    suspend fun validateCard(card: Int)

    suspend fun validateQr(qrCode: String)
}