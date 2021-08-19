package com.remcoil.domain.device

interface SlotOpener {
    suspend fun open(slotNumber: Int): Boolean
}
