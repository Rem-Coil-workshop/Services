package com.remcoil.domain.device

interface SlotOpener {
    suspend fun openByQrCode(qrCode: String): Boolean

    suspend fun openById(id: Int): Boolean
}
