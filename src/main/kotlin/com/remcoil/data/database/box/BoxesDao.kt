package com.remcoil.data.database.box

import com.remcoil.exception.box.NoSuchBoxException
import com.remcoil.data.model.box.Box
import com.remcoil.data.database.task.Tasks
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

class BoxesDao(private val database: Database) {
    suspend fun getBoxById(id: Int): Box = safetySuspendTransaction(database) {
        Boxes
            .select { Boxes.id eq id }
            .map(::extractBox)
            .firstOrNull() ?: throw NoSuchBoxException("Ящика с таким идентификатором не существует")
    }

    suspend fun getBoxByTaskId(taskId: Int): Box? = safetySuspendTransaction(database) {
        Boxes
            .select { Boxes.taskId eq taskId }
            .map(::extractBox)
            .firstOrNull()
    }

    suspend fun getAllBoxes(): List<Box> = safetySuspendTransaction(database) {
        Boxes
            .selectAll()
            .map(::extractBox)
    }

    suspend fun createBox(box: Box): Box =
        safetySuspendTransaction(database, "Значение номера ящика не уникально") {
            val id = Boxes.insertAndGetId {
                it[number] = box.number
                if (box.taskId != null) it[taskId] = EntityID(box.taskId, Tasks)
            }

            box.copy(id = id.value)
        }

    suspend fun updateBox(box: Box): Box =
        safetySuspendTransaction(database, "Введенное значение задачи не существует") {
            Boxes.update({ Boxes.id eq box.id }) {
                it[taskId] =
                    if (box.taskId != null) EntityID(box.taskId, Tasks)
                    else null
            }

            return@safetySuspendTransaction box
        }

    private fun extractBox(row: ResultRow): Box = Box(
        row[Boxes.id].value,
        row[Boxes.number],
        row[Boxes.taskId]?.value
    )
}