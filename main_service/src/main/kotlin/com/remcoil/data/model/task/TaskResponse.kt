package com.remcoil.data.model.task

import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(val qrCode: String)