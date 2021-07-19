package com.remcoil.tasks

import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.*

class TasksDao(private val database: Database) {
    suspend fun getAll(): List<Task> = safetySuspendTransaction(database) {
        Tasks.selectAll().map(::extractTask)
    }

    suspend fun getTaskById(id: Int): Task = safetySuspendTransaction(database) {
        Tasks
            .select { Tasks.id eq id }
            .map(::extractTask)
            .singleOrNull() ?: throw NoSuchTaskException("Задача с таким id не существет")
    }

    suspend fun getTaskQrCode(qrCode: String): Task? = safetySuspendTransaction(database) {
        Tasks
            .select { Tasks.qrCode eq qrCode }
            .map(::extractTask)
            .singleOrNull()
    }

    suspend fun addTask(qrCode: String): Task = safetySuspendTransaction(database, "Введено не уникальное значение кода") {
        val id = Tasks.insertAndGetId {
            it[this.qrCode] = qrCode
        }

        Task(id.value, qrCode)
    }

    suspend fun removeTaskById(id: Int) = safetySuspendTransaction(database) {
        val resultCode = Tasks.deleteWhere { Tasks.id eq id }
        if (resultCode == 0) throw NoSuchTaskException("Задача с таким кодом не существет")
    }

    suspend fun removeTaskByQrCode(qrCode: String) = safetySuspendTransaction(database) {
        val resultCode = Tasks.deleteWhere { Tasks.qrCode eq qrCode }
        if (resultCode == 0) throw NoSuchTaskException("Задача с таким кодом не существет")
    }

    private fun extractTask(row: ResultRow): Task = Task(
        row[Tasks.id].value,
        row[Tasks.qrCode]
    )
}