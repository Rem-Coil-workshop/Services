package com.remcoil.data.database.user

import com.remcoil.data.model.user.User
import com.remcoil.exception.auth.NoSuchUserException
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.*

class UsersDao(private val database: Database) {
    suspend fun getAllUsers(): List<User> = safetySuspendTransaction(database) {
        (Roles innerJoin Users)
            .slice(Users.id, Users.firstname, Users.lastname, Users.password, Roles.title)
            .selectAll()
            .map(::extractUser)
    }


    suspend fun getUser(firstname: String, lastname: String): User? =
        getUserByCondition(Op.build { Users.firstname eq firstname and (Users.lastname eq lastname) })

    private suspend fun getUserByCondition(condition: Op<Boolean>): User? = safetySuspendTransaction(database) {
        (Roles innerJoin Users)
            .slice(Users.id, Users.firstname, Users.lastname, Users.password, Roles.title)
            .select(condition)
            .map(::extractUser)
            .singleOrNull()
    }

    suspend fun createUser(user: User, roleId: Int): User = safetySuspendTransaction(database) {
        val id = Users.insertAndGetId {
            it[firstname] = user.firstname
            it[lastname] = user.lastname
            it[password] = user.password
            it[role_id] = roleId
        }

        user.copy(id = id.value)
    }

    suspend fun removeUser(user: User) = safetySuspendTransaction(database) {
        val resultCode = Users.deleteWhere { Users.firstname eq user.firstname and (Users.lastname eq user.lastname) }
        if (resultCode == 0) throw NoSuchUserException(user.firstname, user.lastname)
    }

    private fun extractUser(row: ResultRow): User = User(
        row[Users.id].value,
        row[Users.firstname],
        row[Users.lastname],
        row[Users.password],
        row[Roles.title]
    )
}