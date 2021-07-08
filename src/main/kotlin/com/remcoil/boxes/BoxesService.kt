package com.remcoil.boxes

import com.remcoil.slots.SlotOpenException
import com.remcoil.tasks.TasksService

class BoxesService(
    private val tasksService: TasksService,
    private val boxesDao: BoxesDao
) {
    suspend fun getAll(): List<Box> = boxesDao.getAllBoxes()

    suspend fun createBox(box: BoxInfo): Box = boxesDao.createBox(box)

    suspend fun updateBox(box: Box): Box {
        val boxFromDB = boxesDao.getBoxById(box.id)

        if (boxFromDB.number != box.number)
            throw NoSuchBoxException("Несоответствующие номера ячеек")

        if (!isTaskUnique(box.taskId))
            throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")

        return boxesDao.updateBox(box)
    }

    private suspend fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || boxesDao.getBoxByTaskId(taskId) == null


    suspend fun getByQrCode(qrCode: String): Box {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw SlotOpenException("Не существует задачи с таким qr кодом (qr code: $qrCode)")

        return boxesDao.getBoxByTaskId(task.id)
            ?: throw SlotOpenException("Ни в одном ящике не хранится такая задача (task id: ${task.id})")
    }
}
