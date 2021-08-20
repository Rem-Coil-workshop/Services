package com.remcoil.data.model.operation

import com.remcoil.data.model.user.User

sealed interface Operation {
    class EmployeeOpenedSlot(val card: Int, val qrCode: String) : Operation
    sealed class UserWithSlot(val user: User, val slotId: Int) : Operation {
        class Open(user: User, slotId: Int) : UserWithSlot(user, slotId)
        class Update(user: User, slotId: Int) : UserWithSlot(user, slotId)
    }
}
