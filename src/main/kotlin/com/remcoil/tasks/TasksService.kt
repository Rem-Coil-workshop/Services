package com.remcoil.tasks

class TasksService(private val dao: TasksDao) {
    suspend fun getTasks(): List<Task> = dao.getAll()

    suspend fun getByQrCode(qrCode: String): Task? = dao.getTaskQrCode(qrCode)

    suspend fun addTask(taskName: String): Task = dao.addTask(taskName)

    suspend fun deleteTask(taskName: String) = dao.removeTask(taskName)
}