package com.remcoil.domain.useCase

import com.remcoil.data.exception.slot.TaskNotUniqueException
import com.remcoil.data.model.slot.Slot
import com.remcoil.data.repository.SlotRepository
import com.remcoil.domain.device.SlotOpener

class SlotUseCase(
    private val slotRepository: SlotRepository,
    private val slotOpener: SlotOpener,
) : BaseUseCase() {

    suspend fun getAll(): List<Slot> = slotRepository.getAll()

    suspend fun getById(id: Int): Slot = slotRepository.getById(id)

    suspend fun create(slot: Slot): Slot {
        if (!isTaskUnique(slot.taskId)) throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        return slotRepository.create(slot)
    }

    suspend fun update(slot: Slot): Slot {
        if (!isTaskUnique(slot.taskId)) throw TaskNotUniqueException("Данная задача уже принадлежит другой ячейке или не существует")
        return slotRepository.update(slot)
    }

    private suspend fun isTaskUnique(taskId: Int?): Boolean =
        taskId == null || slotRepository.getByTaskId(taskId) == null

    suspend fun open(slotId: Int) {
        val slot = slotRepository.getById(slotId)
        slotOpener.open(slot.number)
    }
}