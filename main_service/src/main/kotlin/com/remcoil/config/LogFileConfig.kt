package com.remcoil.config

data class LogFileConfig(
    val jobLogFolder: String,
    val serverLogFolder: String,
    val baseFileName: String,
    val fileExtension: String,
)
