package com.remcoil.slots

interface SlotOpener {
    suspend fun open(qrCode: String): Boolean
}
