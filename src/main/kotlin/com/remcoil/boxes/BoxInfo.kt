package com.remcoil.boxes

import kotlinx.serialization.Serializable

@Serializable
data class BoxInfo(
    val number: Int,
    val taskId: Int? = null
)
