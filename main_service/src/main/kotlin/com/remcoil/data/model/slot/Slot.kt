package com.remcoil.data.model.slot

import kotlinx.serialization.Serializable

@Serializable
data class Slot(
    val id: Int? = null,
    val number: Int,
    val taskId: Int? = null
)