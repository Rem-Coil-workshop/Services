package com.remcoil.config.hocon

data class LogFileConfig(
    val jobLogFolder: String,
    val serverLogFolder: String,
    val baseFileName: String,
    val fileExtension: String,
)
