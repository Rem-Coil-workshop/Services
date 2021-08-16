package com.remcoil.data.database.slot

import com.remcoil.data.exception.slot.NoSuchSlotException
import com.remcoil.data.model.slot.Slot
import com.remcoil.data.database.task.Tasks
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

class SlotsDao(private val database: Database) {
    suspend fun getAllSlots(): List<Slot> = safetySuspendTransaction(database) {
        Slots
            .selectAll()
            .map(::extractSlot)
    }

    suspend fun getSlotById(id: Int): Slot = safetySuspendTransaction(database) {
        Slots
            .select { Slots.id eq id }
            .map(::extractSlot)
            .firstOrNull() ?: throw NoSuchSlotException("Ящика с таким идентификатором не существует")
    }

    suspend fun getSlotByTaskId(taskId: Int): Slot? = safetySuspendTransaction(database) {
        Slots
            .select { Slots.taskId eq taskId }
            .map(::extractSlot)
            .firstOrNull()
    }

    suspend fun createSlot(slot: Slot): Slot =
        safetySuspendTransaction(database, "Значение номера ячейки не уникально") {
            val id = Slots.insertAndGetId {
                it[number] = slot.number
                if (slot.taskId != null) it[taskId] = EntityID(slot.taskId, Tasks)
            }

            slot.copy(id = id.value)
        }

    suspend fun updateSlot(slot: Slot): Slot =
        safetySuspendTransaction(database, "Введенное значение задачи не существует") {
            Slots.update({ Slots.id eq slot.id }) {
                it[taskId] =
                    if (slot.taskId != null) EntityID(slot.taskId, Tasks)
                    else null
            }

            return@safetySuspendTransaction slot
        }

    private fun extractSlot(row: ResultRow): Slot = Slot(
        row[Slots.id].value,
        row[Slots.number],
        row[Slots.taskId]?.value
    )
}