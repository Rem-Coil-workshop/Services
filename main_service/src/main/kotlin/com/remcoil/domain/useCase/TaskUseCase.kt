package com.remcoil.domain.useCase

import com.remcoil.data.model.slot.Slot
import com.remcoil.data.model.task.Task
import com.remcoil.data.repository.SlotRepository
import com.remcoil.data.repository.TaskRepository

class TaskUseCase(
    private val taskRepository: TaskRepository,
    private val slotRepository: SlotRepository
) : BaseUseCase() {
    suspend fun getAll(): List<Task> = taskRepository.getAll()

    suspend fun add(taskName: String): Task = taskRepository.add(taskName)

    suspend fun delete(taskId: Int) = taskRepository.delete(taskId)

    suspend fun delete(qrCode: String) = taskRepository.delete(qrCode)

    suspend fun whichSlot(qrCode: String): Slot? {
        val task = taskRepository.getByQrCode(qrCode) ?: return null
        return slotRepository.getByTaskId(task.id)
    }
}