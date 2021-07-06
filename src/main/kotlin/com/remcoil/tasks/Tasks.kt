package com.remcoil.tasks

import org.jetbrains.exposed.dao.id.IntIdTable

object Tasks : IntIdTable() {
    val name = varchar("name", 64)
}