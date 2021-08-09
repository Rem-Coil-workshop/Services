package com.remcoil.data.model.task

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val qrCode: String,
)
