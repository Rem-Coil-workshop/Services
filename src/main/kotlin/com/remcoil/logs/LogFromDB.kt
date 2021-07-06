package com.remcoil.logs

import java.time.LocalDateTime

data class LogFromDB(
    val id: Int,
    val employeeId: Int,
    val date: LocalDateTime,
    val taskId: Int
)
