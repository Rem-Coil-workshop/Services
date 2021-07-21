package com.remcoil.di.user

import com.remcoil.data.database.user.RolesDao
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.rolesComponent() {
    bind<RolesDao>() with singleton { RolesDao(instance()) }
}