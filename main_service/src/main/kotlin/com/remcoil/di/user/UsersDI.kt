package com.remcoil.di.user

import com.remcoil.data.database.user.UsersDao
import com.remcoil.data.model.user.Token
import com.remcoil.data.model.user.User
import com.remcoil.gateway.service.user.UsersService
import org.kodein.di.*

fun DI.Builder.usersComponent() {
    bind<UsersDao>() with singleton { UsersDao(instance()) }

    bind<UsersService>() with singleton { UsersService(instance(), instance()) }

    bind<Token>() with factory { user: User -> Token(user, instance()) }
}