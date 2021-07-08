package com.remcoil.boxes

import kotlinx.serialization.Serializable

@Serializable
data class Box(
    val id: Int,
    val number: Int,
    val taskId: Int? = null
)