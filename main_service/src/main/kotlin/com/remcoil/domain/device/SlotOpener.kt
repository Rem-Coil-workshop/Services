package com.remcoil.domain.device

interface SlotOpener {
    suspend fun openByQrCode(qrCode: String): Boolean

    suspend fun openByBoxNumber(id: Int): Boolean
}
