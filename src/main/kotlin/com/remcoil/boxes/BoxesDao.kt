package com.remcoil.boxes

import com.remcoil.tasks.Tasks
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class BoxesDao(private val database: Database) {
    fun getBoxById(id: Int): Box = transaction(database) {
        Boxes
            .select { Boxes.id eq id }
            .map(::extractBox)
            .singleOrNull() ?: throw NoSuchBoxException()
    }

    fun getAllBoxes(): List<Box> = transaction(database) {
        Boxes
            .selectAll()
            .map(::extractBox)
    }

    fun createBox(box: BoxInfo): Box = transaction {
        val id = Boxes.insertAndGetId {
            it[number] = box.number
            if (box.taskId != null) it[taskId] = EntityID(box.taskId, Tasks)
        }

        Box(id.value, box.number, box.taskId)
    }

    private fun extractBox(row: ResultRow): Box = Box(
        row[Boxes.id].value,
        row[Boxes.number],
        row[Boxes.taskId]?.value
    )

    fun updateBox(box: Box): Box = transaction(database) {
        Boxes.update({ Boxes.id eq box.id}) {
            if (box.taskId != null) it[taskId] = EntityID(box.taskId, Tasks)
            else it[taskId] = null
        }

        return@transaction box
    }

    fun getBoxByTaskId(taskId: Int): Box? = transaction {
        Boxes
            .select { Boxes.taskId eq taskId }
            .map(::extractBox)
            .singleOrNull()
    }
}