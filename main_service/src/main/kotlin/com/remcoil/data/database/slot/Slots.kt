package com.remcoil.data.database.slot

import com.remcoil.data.database.task.Tasks
import org.jetbrains.exposed.dao.id.IntIdTable

object Slots : IntIdTable(name = "BOXES") {
    val number = integer("box_number")
    val taskId = reference("task_id", Tasks).nullable()
}