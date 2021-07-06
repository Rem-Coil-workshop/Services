package com.remcoil.boxes

import com.remcoil.tasks.Task
import kotlinx.serialization.Serializable

@Serializable
data class Box(
    val id: Int,
    val number: Int,
    val task: Task
)