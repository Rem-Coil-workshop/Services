package com.remcoil.gateway.service.task

import com.remcoil.data.database.task.TasksDao
import com.remcoil.data.model.task.Task
import com.remcoil.utils.logger

class TasksService(private val dao: TasksDao) {
    suspend fun getAll(): List<Task> {
        val tasks = dao.getAll()
        logger.info("Отдано ${tasks.size} задач")
        return tasks
    }

    suspend fun getByIds(ids: List<Int>): List<Task> {
        val tasks = dao.getTasksByIds(ids)
        logger.info("Отдали ${tasks.size} задач")
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

    suspend fun deleteTask(taskId: Int) {
        dao.removeTaskById(taskId)
        logger.info("Задача $taskId удалена")
    }

    suspend fun deleteTask(qrCode: String) {
        dao.removeTaskByQrCode(qrCode)
        logger.info("Задача $qrCode удалена")
    }
}