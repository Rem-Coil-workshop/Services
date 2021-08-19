package com.remcoil.domain.message

import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.OperationSaver
import java.time.LocalTime
import java.time.format.DateTimeFormatter

abstract class MessageGenerator <T : OperationWithData> constructor(private val saver: OperationSaver) {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    @Suppress("UNCHECKED_CAST")
    fun onOperation(operation: OperationWithData) {
        if (isCorrectOperation(operation)) {
            val message = generate(operation as T)
            saver.save(message)
        }
    }

    protected abstract fun isCorrectOperation(operation: OperationWithData): Boolean

    protected abstract fun generate(operation: T): String

    protected fun generateMessage(entity: String, action: String): String {
        val time = LocalTime.now()
        return "[${time.format(formatter)}] $entity $action"
    }
}