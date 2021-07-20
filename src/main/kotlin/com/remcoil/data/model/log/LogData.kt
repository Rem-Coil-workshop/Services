package com.remcoil.data.model.log

import kotlinx.serialization.Serializable

@Serializable
data class LogData(val qrCode: String, val cardCode: Int)