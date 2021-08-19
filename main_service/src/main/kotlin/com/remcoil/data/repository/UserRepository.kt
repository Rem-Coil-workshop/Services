package com.remcoil.data.repository

import com.remcoil.data.database.user.RolesDao
import com.remcoil.data.database.user.UsersDao
import com.remcoil.data.exception.user.NoSuchRoleException
import com.remcoil.data.exception.user.NoSuchUserException
import com.remcoil.data.model.user.User
import com.remcoil.utils.logged
import com.remcoil.utils.loggedEntity

class UserRepository(
    private val usersDao: UsersDao,
    private val rolesDao: RolesDao,
) {
    suspend fun getAll(): List<User> = logged("Отдали всех пользователей") {
        return usersDao.getAllUsers()
    }

    suspend fun get(firstName: String, lastName: String): User = usersDao.getUser(firstName, lastName)
        ?: throw NoSuchUserException(firstName, lastName)

    suspend fun create(role: String, user: User): User =
        loggedEntity({ "Пользователь ${it.firstname} ${it.lastname} создан" }) {
            val roleFromDb = rolesDao.getRoleByTitle(role)
                ?: throw NoSuchRoleException("Не существует такой роли пользователя (${role})")

            usersDao.createUser(user, roleFromDb.id)
        }

    suspend fun remove(user: User) = logged("Пользователь ${user.firstname} ${user.lastname} удалён") {
        usersDao.removeUser(user)
    }
}