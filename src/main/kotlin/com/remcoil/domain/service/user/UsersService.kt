package com.remcoil.domain.service.user

import com.remcoil.data.database.user.RolesDao
import com.remcoil.data.database.user.UsersDao
import com.remcoil.data.model.user.User
import com.remcoil.data.model.user.UserCredentials
import com.remcoil.exception.user.NoSuchRoleException
import com.remcoil.exception.auth.NoSuchUserException
import com.remcoil.exception.auth.WrongPasswordException
import com.remcoil.utils.logger

class UsersService(
    private val usersDao: UsersDao,
    private val rolesDao: RolesDao,
) {
    suspend fun getUser(credentials: UserCredentials): User {
        val user = usersDao.getUser(credentials.firstname, credentials.lastname)
            ?: throw NoSuchUserException(credentials.firstname, credentials.lastname)

        return if (user.password == credentials.password) {
            logger.info("Пользователь ${user.firstname} ${user.lastname} авторизован")
            user
        }
        else throw WrongPasswordException()
    }

    suspend fun createByCredentials(credentials: UserCredentials): User {
        val role = rolesDao.getRoleByTitle(credentials.role.uppercase())
            ?: throw NoSuchRoleException("Не существует такой роли пользователя (${credentials.role})")
        val user = User(credentials)
        val newUser = usersDao.createUser(user, role.id)
        logger.info("Пользователь ${user.firstname} ${user.lastname} создан")
        return newUser
    }
}