package com.remcoil.gateway.service.task

import com.remcoil.data.database.task.TasksDao
import com.remcoil.data.model.task.Task
import com.remcoil.utils.logged
import com.remcoil.utils.loggedEntity

class TasksService(private val dao: TasksDao) {
    suspend fun getAll(): List<Task> = loggedEntity({ "Отдано ${it.size} задач" }) {
        dao.getAll()
    }

    suspend fun getByIds(ids: List<Int>): List<Task> = loggedEntity({ "Отдали ${it.size} задач" }) {
        dao.getTasksByIds(ids)
    }

    suspend fun getByQrCode(qrCode: String): Task? = loggedEntity({ "Отдана задача ${it?.qrCode}" }) {
        dao.getTaskQrCode(qrCode)
    }

    suspend fun add(taskName: String): Task = loggedEntity({ "Создана задача ${it.qrCode}" }) {
        dao.addTask(taskName)
    }

    suspend fun delete(taskId: Int) = logged("Задача $taskId удалена") {
        dao.removeTaskById(taskId)
    }

    suspend fun delete(qrCode: String) = logged("Задача $qrCode удалена") {
        dao.removeTaskByQrCode(qrCode)
    }
}