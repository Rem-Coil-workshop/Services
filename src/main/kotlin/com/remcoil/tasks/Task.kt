package com.remcoil.tasks

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val qrCode: String,
)
