package com.remcoil.data.repository

import com.remcoil.data.database.slot.SlotsDao
import com.remcoil.data.exception.slot.NoSuchSlotException
import com.remcoil.data.model.slot.Slot
import com.remcoil.utils.loggedEntity

class SlotRepository(private val dao: SlotsDao) {
    suspend fun getAll(): List<Slot> = loggedEntity({ "Отдал ${it.size} ячеек" }) {
        dao.getAllSlots()
    }

    suspend fun getById(id: Int): Slot = loggedEntity({ "Отдал ячейку с id ${it.id}" }) {
        dao.getSlotById(id)
    }

    suspend fun create(slot: Slot): Slot = loggedEntity({ "Создана ячейка ${it.number}" }) {
        dao.createSlot(slot)
    }

    suspend fun update(slot: Slot): Slot = loggedEntity({ "Обновлена ячейка ${it.number}" }) {
        if (slot.id == null) throw NoSuchSlotException("Передана ячейка с нулевым id")
        val slotFromDB = dao.getSlotById(slot.id)

        if (slotFromDB.number != slot.number) throw NoSuchSlotException("Несоответствующие номера ячеек")
        return@loggedEntity dao.updateSlot(slot)
    }

    suspend fun getByTaskId(taskId: Int): Slot? = dao.getSlotByTaskId(taskId)
}