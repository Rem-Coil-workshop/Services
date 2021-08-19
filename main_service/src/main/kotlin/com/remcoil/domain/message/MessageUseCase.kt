package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData

class MessageUseCase(private val generators: List<MessageGenerator<*>>) {
    fun saveOperation(operation: OperationWithData) = generators.forEach { it.onOperation(operation) }
}