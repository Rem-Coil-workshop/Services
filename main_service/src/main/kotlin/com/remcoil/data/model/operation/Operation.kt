package com.remcoil.data.model.operation

sealed interface Operation {
    class SlotOpened(val card: Int, val qrCode: String) : Operation
}
