package com.remcoil.domain.files

interface OperationHistorySaver {
    fun save(message: String)
}