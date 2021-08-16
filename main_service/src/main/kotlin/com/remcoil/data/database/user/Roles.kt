package com.remcoil.data.database.user

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Roles : IdTable<Int>("USER_ROLES") {
    override val id: Column<EntityID<Int>> = integer("id").entityId()
    val title = varchar("title", 64)
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}