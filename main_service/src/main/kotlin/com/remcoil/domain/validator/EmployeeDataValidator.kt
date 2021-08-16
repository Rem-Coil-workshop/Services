package com.remcoil.domain.validator

interface EmployeeDataValidator {
    suspend fun validateCard(card: Int)

    suspend fun validateQr(qrCode: String)

    suspend fun checkPermission(card: Int, qrCode: String): Boolean
}