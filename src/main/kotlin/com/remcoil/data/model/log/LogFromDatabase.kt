package com.remcoil.data.model.log

import java.time.LocalDateTime

data class LogFromDatabase(
    val id: Int,
    val employeeId: Int,
    val date: LocalDateTime,
    val taskId: Int
)
