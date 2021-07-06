package com.remcoil.logs

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.logsComponents() {
    bind<LogsDao>() with singleton { LogsDao(instance()) }
    bind<LogsService>() with singleton { LogsService(instance(), instance(), instance()) }
}