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
        return boxesDao.updateBox(box)
    }

    fun getByQrCode(qrCode: String): Box {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw SlotOpenException("No task with such qr code (qr code: $qrCode)")

        return getByTaskId(task.id)
            ?: throw SlotOpenException("No box with such task (task id: ${task.id})")
    }

    private fun getByTaskId(taskId: Int): Box? = boxesDao.getBoxByTaskId(taskId)
}
