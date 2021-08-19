package com.remcoil.domain.useCase

import com.remcoil.data.exception.user.WrongPasswordException
import com.remcoil.data.model.user.User
import com.remcoil.data.model.user.UserCredentials
import com.remcoil.data.repository.UserRepository
import com.remcoil.utils.logged
import com.remcoil.utils.loggedEntity
import com.remcoil.utils.takeIfOrThrow

class UserUseCase(private val userRepository: UserRepository) {
    suspend fun getAll(): List<User> = logged("Отдали всех пользователей") {
        return userRepository.getAll()
    }

    suspend fun get(credentials: UserCredentials): User =
        loggedEntity({ "Пользователь ${it.firstname} ${it.lastname} авторизован" }) {
            userRepository.get(credentials.firstname, credentials.lastname)
                .takeIfOrThrow(WrongPasswordException()) { checkPassword(credentials) }
        }

    suspend fun createByCredentials(credentials: UserCredentials): User {
        val user = User(credentials)
        return userRepository.create(credentials.userRole, user)
    }

    suspend fun remove(user: User) = userRepository.remove(user)
}