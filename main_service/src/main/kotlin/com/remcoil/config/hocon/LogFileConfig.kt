package com.remcoil.config.hocon

data class LogFileConfig(
    val logsFolder: String,
    val operationHistoryFolder: String,
    val fileExtension: String,
)
