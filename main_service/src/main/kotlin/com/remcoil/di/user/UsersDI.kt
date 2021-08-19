package com.remcoil.di.user

import com.remcoil.data.database.user.UsersDao
import com.remcoil.data.model.user.Token
import com.remcoil.data.model.user.User
import com.remcoil.data.repository.UserRepository
import com.remcoil.domain.useCase.UserUseCase
import org.kodein.di.*

fun DI.Builder.usersComponent() {
    bind<UsersDao>() with singleton { UsersDao(instance()) }
    bind<UserRepository>() with singleton { UserRepository(instance(), instance()) }
    bind<UserUseCase>() with singleton { UserUseCase(instance()) }

    bind<Token>() with factory { user: User -> Token(user, instance()) }
}