package com.remcoil.boxes

import com.remcoil.slots.SlotOpenException
import com.remcoil.tasks.TasksService
import com.remcoil.utils.logger

class BoxesService(
    private val tasksService: TasksService,
    private val boxesDao: BoxesDao
) {
    suspend fun getAll(): List<Box> {
        val boxes = boxesDao.getAllBoxes()
        logger.info("Отдал ${boxes.size} ячеек")
        return boxes
    }

    suspend fun createBox(box: BoxInfo): Box {
        val newBox = boxesDao.createBox(box)
        logger.info("Создана ячейка ${newBox.number}")
        return newBox
    }

    suspend fun updateBox(box: Box): Box {
        val boxFromDB = boxesDao.getBoxById(box.id)

        if (boxFromDB.number != box.number) {
            logger.info("Несоответствующие номера ячеек")
            throw NoSuchBoxException("Несоответствующие номера ячеек")
        }

        if (!isTaskUnique(box.taskId)) {
            logger.info("Данная задача уже принадлежит другой ячейке или не существует")
            throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        }
        val updatedBox = boxesDao.updateBox(box)
        logger.info("Обновлена ячейка ${updatedBox.number}")
        return updatedBox
    }

    private suspend fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || boxesDao.getBoxByTaskId(taskId) == null


    suspend fun getByQrCode(qrCode: String): Box {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw logSlotException("Не существует задачи с таким qr кодом (qr code: $qrCode)")

        return boxesDao.getBoxByTaskId(task.id)
            ?: throw logSlotException("Ни в одном ящике не хранится такая задача (task id: ${task.id})")
    }

    private fun logSlotException(message: String): SlotOpenException {
        logger.info(message)
        return SlotOpenException(message)
    }
}
