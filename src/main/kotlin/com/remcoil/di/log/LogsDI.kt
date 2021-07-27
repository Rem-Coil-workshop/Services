package com.remcoil.di.log

import com.remcoil.data.database.log.LogsDao
import com.remcoil.domain.service.log.LogsService
import com.remcoil.domain.useCase.files.DirectoryHelper
import com.remcoil.domain.useCase.files.DirectoryHelperImpl
import com.remcoil.domain.useCase.files.OperationLogger
import com.remcoil.domain.useCase.files.OperationLoggerImpl
import com.remcoil.domain.useCase.log.LogMessageGenerator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.logsComponents() {
    bind<LogsDao>() with singleton { LogsDao(instance()) }
    bind<LogsService>() with singleton { LogsService(instance(), instance(), instance()) }

    bind<OperationLogger>() with singleton { OperationLoggerImpl(instance()) }
    bind<DirectoryHelper>() with singleton { DirectoryHelperImpl(instance()) }

    bind<LogMessageGenerator>() with singleton { LogMessageGenerator(instance()) }
}