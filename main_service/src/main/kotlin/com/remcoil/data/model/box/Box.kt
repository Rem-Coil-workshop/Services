package com.remcoil.data.model.box

import kotlinx.serialization.Serializable

@Serializable
data class Box(
    val id: Int? = null,
    val number: Int,
    val taskId: Int? = null
)