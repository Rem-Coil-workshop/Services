package com.remcoil.tasks

import com.remcoil.utils.logger

class TasksService(private val dao: TasksDao) {
    suspend fun getTasks(): List<Task> {
        val tasks = dao.getAll()
        logger.info("Отдано ${tasks.size} задач")
        return tasks
    }

    suspend fun getByQrCode(qrCode: String): Task? {
        val task = dao.getTaskQrCode(qrCode)
        logger.info("Отдана задача ${task?.qrCode}")
        return task
    }

    suspend fun addTask(taskName: String): Task {
        val task = dao.addTask(taskName)
        logger.info("Создана задача ${task.qrCode}")
        return task
    }

    suspend fun deleteTask(taskName: String) {
        dao.removeTask(taskName)
        logger.info("Задача $taskName удалена")
    }
}