package com.remcoil.data.database.user

import com.remcoil.data.model.user.UserRole
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class RolesDao(private val database: Database) {
    suspend fun getRoleByTitle(title: String): UserRole? = safetySuspendTransaction(database) {
        Roles
            .select { Roles.title eq title }
            .map(::extractRole)
            .singleOrNull()
    }

    private fun extractRole(row: ResultRow): UserRole = UserRole(
        row[Roles.id].value,
        row[Roles.title]
    )
}