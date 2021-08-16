package com.remcoil.data.database.task

import com.remcoil.data.exception.task.NoSuchTaskException
import com.remcoil.data.model.task.Task
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.*

class TasksDao(private val database: Database) {
    suspend fun getAll(): List<Task> = safetySuspendTransaction(database) {
        Tasks.selectAll().map(::extractTask)
    }

    suspend fun getTasksByIds(tasksId: List<Int>): List<Task> = safetySuspendTransaction(database) {
        Tasks
            .select { Tasks.id.inList(tasksId) }
            .map(::extractTask)
    }

    suspend fun getTaskQrCode(qrCode: String): Task? = safetySuspendTransaction(database) {
        Tasks
            .select { Tasks.qrCode eq qrCode }
            .map(::extractTask)
            .singleOrNull()
    }

    suspend fun addTask(qrCode: String): Task =
        safetySuspendTransaction(database, "Введено не уникальное значение кода") {
            val id = Tasks.insertAndGetId {
                it[Tasks.qrCode] = qrCode
            }
            Task(id.value, qrCode)
        }

    suspend fun removeTaskById(id: Int) = safetySuspendTransaction(database) {
        val resultCode = Tasks.deleteWhere { Tasks.id eq id }
        if (resultCode == 0) throw NoSuchTaskException("Задача с таким кодом не существует")
    }

    suspend fun removeTaskByQrCode(qrCode: String) = safetySuspendTransaction(database) {
        val resultCode = Tasks.deleteWhere { Tasks.qrCode eq qrCode }
        if (resultCode == 0) throw NoSuchTaskException("Задача с таким кодом не существует")
    }

    private fun extractTask(row: ResultRow): Task = Task(
        row[Tasks.id].value,
        row[Tasks.qrCode]
    )
}