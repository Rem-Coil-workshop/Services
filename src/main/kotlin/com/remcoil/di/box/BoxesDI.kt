package com.remcoil.di.box

import com.remcoil.data.database.box.BoxesDao
import com.remcoil.useCase.service.box.BoxesService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.boxesComponents() {
    bind<BoxesDao>() with singleton { BoxesDao((instance())) }
    bind<BoxesService>() with singleton { BoxesService(instance(), instance()) }
}