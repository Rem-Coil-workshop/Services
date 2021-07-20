package com.remcoil.presentation.device

interface SlotOpener {
    suspend fun openByQrCode(qrCode: String): Boolean

    suspend fun openByBoxNumber(id: Int): Boolean
}
