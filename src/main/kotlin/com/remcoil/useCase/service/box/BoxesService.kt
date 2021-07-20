package com.remcoil.useCase.service.box

import com.remcoil.data.database.box.BoxesDao
import com.remcoil.exception.box.NoSuchBoxException
import com.remcoil.exception.box.TaskNotUniqueException
import com.remcoil.data.model.box.Box
import com.remcoil.exception.slot.SlotOpenException
import com.remcoil.useCase.service.task.TasksService
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

    suspend fun getById(id: Int): Box {
        val box = boxesDao.getBoxById(id)
        logger.info("Отдал яейку с id ${box.id}")
        return box
    }

    suspend fun createBox(box: Box): Box {
        val newBox = boxesDao.createBox(box)
        logger.info("Создана ячейка ${newBox.number}")
        return newBox
    }

    suspend fun updateBox(box: Box): Box {
        if (box.id == null) throw NoSuchBoxException("Передана ячейка с нулевым id")

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

    suspend fun isQrCodeExist(qrCode: String): Boolean {
        return try {
            getByQrCode(qrCode)
            true
        } catch (e: SlotOpenException) {
            false
        }
    }
}
