package com.remcoil.gateway.service.user

import com.remcoil.data.database.user.RolesDao
import com.remcoil.data.database.user.UsersDao
import com.remcoil.data.exception.user.NoSuchRoleException
import com.remcoil.data.exception.user.NoSuchUserException
import com.remcoil.data.exception.user.WrongPasswordException
import com.remcoil.data.model.user.User
import com.remcoil.data.model.user.UserCredentials
import com.remcoil.utils.logged
import com.remcoil.utils.loggedEntity
import com.remcoil.utils.takeIfOrThrow

class UsersService(
    private val usersDao: UsersDao,
    private val rolesDao: RolesDao,
) {
    suspend fun getAll(): List<User> = logged("Отдали всех пользователей") {
        return usersDao.getAllUsers()
    }

    suspend fun get(credentials: UserCredentials): User =
        loggedEntity({ "Пользователь ${it.firstname} ${it.lastname} авторизован" }) {
            usersDao.getUser(credentials.firstname, credentials.lastname)
                ?.takeIfOrThrow(WrongPasswordException()) { checkPassword(credentials) }
                ?: throw NoSuchUserException(credentials.firstname, credentials.lastname)
        }

    suspend fun createByCredentials(credentials: UserCredentials): User =
        loggedEntity({ "Пользователь ${it.firstname} ${it.lastname} создан" }) {
            val role = rolesDao.getRoleByTitle(credentials.role.uppercase())
                ?: throw NoSuchRoleException("Не существует такой роли пользователя (${credentials.role})")

            usersDao.createUser(User(credentials), role.id)
        }

    suspend fun remove(user: User) = logged("Пользователь ${user.firstname} ${user.lastname} удалён") {
        usersDao.removeUser(user)
    }
}