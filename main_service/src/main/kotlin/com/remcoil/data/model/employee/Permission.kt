package com.remcoil.data.model.employee

import com.remcoil.data.model.task.Task
import kotlinx.serialization.Serializable

@Serializable
data class Permission(val employee: Employee, val task: Task)
