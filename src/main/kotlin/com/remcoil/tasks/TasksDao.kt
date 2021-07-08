package com.remcoil.tasks

import com.remcoil.utils.safetyTransaction
import org.jetbrains.exposed.sql.*

class TasksDao(private val database: Database) {
    fun getAll(): List<Task> = safetyTransaction(database) {
        Tasks.selectAll().map(::extractTask)
    }

    fun getTaskById(id: Int): Task = safetyTransaction(database) {
        Tasks
            .select { Tasks.id eq id }
            .map(::extractTask)
            .singleOrNull() ?: throw NoSuchTaskException("Задача с таким id не существет")
    }

    fun getTaskQrCode(qrCode: String): Task? = safetyTransaction(database) {
        Tasks
            .select { Tasks.qrCode eq qrCode }
            .map(::extractTask)
            .singleOrNull()
    }

    fun addTask(qrCode: String): Task = safetyTransaction(database, "Введено не уникальное значение кода") {
        val id = Tasks.insertAndGetId {
            it[this.qrCode] = qrCode
        }

        Task(id.value, qrCode)
    }

    fun removeTask(qrCode: String) = safetyTransaction(database) {
        val resultCode = Tasks.deleteWhere { Tasks.qrCode eq qrCode }
        if (resultCode == 0) throw NoSuchTaskException("Задача с таким кодом не существет")
    }

    private fun extractTask(row: ResultRow): Task = Task(
        row[Tasks.id].value,
        row[Tasks.qrCode]
    )
}