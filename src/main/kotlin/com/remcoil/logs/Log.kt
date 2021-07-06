package com.remcoil.logs

import kotlinx.serialization.Serializable

@Serializable
data class Log(
    val id: Int,
    val workerName: String,
    val workerSurname: String,
    val date: String,
    val task: String
)