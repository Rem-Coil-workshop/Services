package com.remcoil.boxes

import com.remcoil.tasks.Tasks
import com.remcoil.utils.safetyTransaction
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

class BoxesDao(private val database: Database) {
    fun getBoxById(id: Int): Box = safetyTransaction(database) {
        Boxes
            .select { Boxes.id eq id }
            .map(::extractBox)
            .firstOrNull() ?: throw NoSuchBoxException("Ящика с таким идентификатором не существует")
    }

    fun getBoxByTaskId(taskId: Int): Box? = safetyTransaction(database) {
        Boxes
            .select { Boxes.taskId eq taskId }
            .map(::extractBox)
            .firstOrNull()
    }

    fun getAllBoxes(): List<Box> = safetyTransaction(database) {
        Boxes
            .selectAll()
            .map(::extractBox)
    }

    fun createBox(box: BoxInfo): Box = safetyTransaction(database, "Значение номера ящика не уникально") {
        val id = Boxes.insertAndGetId {
            it[number] = box.number
            if (box.taskId != null) it[taskId] = EntityID(box.taskId, Tasks)
        }

        Box(id.value, box.number, box.taskId)
    }

    fun updateBox(box: Box): Box = safetyTransaction(database) {
        Boxes.update({ Boxes.id eq box.id }) {
            it[taskId] =
                if (box.taskId != null) EntityID(box.taskId, Tasks)
                else null
        }

        return@safetyTransaction box
    }

    private fun extractBox(row: ResultRow): Box = Box(
        row[Boxes.id].value,
        row[Boxes.number],
        row[Boxes.taskId]?.value
    )
}