package com.remcoil.di.log

import com.remcoil.data.database.log.LogsDao
import com.remcoil.useCase.service.log.LogsService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.logsComponents() {
    bind<LogsDao>() with singleton { LogsDao(instance()) }
    bind<LogsService>() with singleton { LogsService(instance(), instance(), instance()) }
}