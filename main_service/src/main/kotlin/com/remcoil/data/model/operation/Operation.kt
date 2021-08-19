package com.remcoil.data.model.operation

sealed interface Operation {
    class EmployeeOpenedSlot(val card: Int, val qrCode: String) : Operation
}
