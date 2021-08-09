package com.remcoil.data.database.box

import com.remcoil.data.database.task.Tasks
import org.jetbrains.exposed.dao.id.IntIdTable

object Boxes : IntIdTable() {
    val number = integer("box_number")
    val taskId = reference("task_id", Tasks).nullable()
}