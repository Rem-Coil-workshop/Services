package com.remcoil.boxes

import com.remcoil.slots.SlotOpenException
import com.remcoil.tasks.TasksService

class BoxesService(
    private val tasksService: TasksService,
    private val boxesDao: BoxesDao
) {
    fun getAll(): List<Box> = boxesDao.getAllBoxes()

    fun createBox(box: BoxInfo): Box = boxesDao.createBox(box)

    fun updateBox(box: Box): Box {
        val boxFromDB = boxesDao.getBoxById(box.id)
        if (boxFromDB.number != box.number) throw NoSuchBoxException()
        if (!isTaskUnique(box.taskId)) throw TaskNotUniqueException()
        return boxesDao.updateBox(box)
    }

    private fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || boxesDao.getBoxByTaskId(taskId) == null


    fun getByQrCode(qrCode: String): Box {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw SlotOpenException("Не существует задачи с таким qr кодом (qr code: $qrCode)")

        return boxesDao.getBoxByTaskId(task.id)
            ?: throw SlotOpenException("Ни в одном ящике не хранится такая задача (task id: ${task.id})")
    }
}
