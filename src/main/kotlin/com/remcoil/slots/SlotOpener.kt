package com.remcoil.slots

interface SlotOpener {
    suspend fun openByQrCode(qrCode: String): Boolean

    suspend fun openByBoxNumber(id: Int): Boolean
}
