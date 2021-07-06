package com.remcoil.tasks

class TasksService(private val dao: TasksDao) {
    fun getTasks(): List<Task> = dao.getAll()

    fun addTask(taskName: String): Task = dao.addTask(taskName)

    fun deleteTask(taskName: String) = dao.removeTask(taskName)
}