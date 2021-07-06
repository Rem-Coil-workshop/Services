package com.remcoil.tasks

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TasksDao(private val database: Database) {
    fun getAll(): List<Task> = transaction(database) {
        Tasks.selectAll().map(::extractTask)
    }

    fun addTask(taskName: String): Task = transaction(database) {
        val id = Tasks.insertAndGetId {
            it[name] = taskName
        }

        Task(id.value, taskName)
    }

    fun removeTask(task: String) = transaction {
        val resultCode = Tasks.deleteWhere { Tasks.name eq task }
        if (resultCode == 0) throw NoSuchTaskException()
    }

    private fun extractTask(row: ResultRow): Task = Task(
        row[Tasks.id].value,
        row[Tasks.name]
    )
}