package com.remcoil.gateway.service.slot

import com.remcoil.data.database.slot.SlotsDao
import com.remcoil.data.exception.slot.NoSuchSlotException
import com.remcoil.data.exception.slot.SlotOpenException
import com.remcoil.data.exception.slot.TaskNotUniqueException
import com.remcoil.data.model.slot.Slot
import com.remcoil.gateway.service.task.TasksService
import com.remcoil.utils.loggedEntity

class SlotsService(
    private val tasksService: TasksService,
    private val slotsDao: SlotsDao
) {
    suspend fun getAll(): List<Slot> = loggedEntity({ "Отдал ${it.size} ячеек" }) {
        slotsDao.getAllSlots()
    }

    suspend fun getById(id: Int): Slot = loggedEntity({ "Отдал ячейку с id ${it.id}" }) {
        slotsDao.getSlotById(id)
    }

    suspend fun create(slot: Slot): Slot = loggedEntity({ "Создана ячейка ${it.number}" }) {
        if (!isTaskUnique(slot.taskId)) throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        slotsDao.createSlot(slot)
    }

    suspend fun update(box: Slot): Slot = loggedEntity({ "Обновлена ячейка ${it.number}" }) {
        if (box.id == null) throw NoSuchSlotException("Передана ячейка с нулевым id")
        val slotFromDB = slotsDao.getSlotById(box.id)

        if (slotFromDB.number != box.number) throw NoSuchSlotException("Несоответствующие номера ячеек")
        if (!isTaskUnique(box.taskId)) throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        return@loggedEntity slotsDao.updateSlot(box)
    }

    private suspend fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || slotsDao.getSlotByTaskId(taskId) == null

    suspend fun getByQrCode(qrCode: String): Slot {
        val task = tasksService.getByQrCode(qrCode)
            ?: throw SlotOpenException("Не существует задачи с таким qr кодом (qr code: $qrCode)")

        return slotsDao.getSlotByTaskId(task.id)
            ?: throw SlotOpenException("Ни в одном ящике не хранится такая задача (task id: ${task.id})")
    }
}
