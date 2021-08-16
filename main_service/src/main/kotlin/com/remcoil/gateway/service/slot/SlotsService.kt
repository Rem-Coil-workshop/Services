package com.remcoil.gateway.service.slot

import com.remcoil.data.database.slot.SlotsDao
import com.remcoil.data.exception.slot.NoSuchSlotException
import com.remcoil.data.exception.slot.SlotOpenException
import com.remcoil.data.exception.slot.TaskNotUniqueException
import com.remcoil.data.model.slot.Slot
import com.remcoil.gateway.service.task.TasksService
import com.remcoil.utils.logger

class SlotsService(
    private val tasksService: TasksService,
    private val slotsDao: SlotsDao
) {
    suspend fun getAll(): List<Slot> {
        val boxes = slotsDao.getAllSlots()
        logger.info("Отдал ${boxes.size} ячеек")
        return boxes
    }

    suspend fun getById(id: Int): Slot {
        val box = slotsDao.getSlotById(id)
        logger.info("Отдал ячейку с id ${box.id}")
        return box
    }

    suspend fun create(box: Slot): Slot {
        val newBox = slotsDao.createSlot(box)
        logger.info("Создана ячейка ${newBox.number}")
        return newBox
    }

    suspend fun update(box:Slot): Slot {
        if (box.id == null) throw NoSuchSlotException("Передана ячейка с нулевым id")

        val slotFromDB = slotsDao.getSlotById(box.id)

        if (slotFromDB.number != box.number) {
            logger.info("Несоответствующие номера ячеек")
            throw NoSuchSlotException("Несоответствующие номера ячеек")
        }

        if (!isTaskUnique(box.taskId)) {
            logger.info("Данная задача уже принадлежит другой ячейке или не существует")
            throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        }

        val updatedSlot = slotsDao.updateSlot(box)
        logger.info("Обновлена ячейка ${updatedSlot.number}")
        return updatedSlot
    }

    private suspend fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || slotsDao.getSlotByTaskId(taskId) == null


    suspend fun getByQrCode(qrCode: String): Slot {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw logSlotException("Не существует задачи с таким qr кодом (qr code: $qrCode)")

        return slotsDao.getSlotByTaskId(task.id)
            ?: throw logSlotException("Ни в одном ящике не хранится такая задача (task id: ${task.id})")
    }

    private fun logSlotException(message: String): SlotOpenException {
        logger.info(message)
        return SlotOpenException(message)
    }
}
