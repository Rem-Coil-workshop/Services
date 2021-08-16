package com.remcoil.data.database.user

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("USER_ACCOUNT") {
    val firstname = varchar("firstname", 64)
    val lastname = varchar("lastname", 64)
    val password = varchar("password", 64)
    val role_id = reference("role_id", Roles)
}