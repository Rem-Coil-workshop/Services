package com.remcoil.di.log

import com.remcoil.config.LogFileConfig
import com.remcoil.domain.service.log.JobLogsService
import com.remcoil.domain.service.log.MainLogsService
import com.remcoil.domain.useCase.files.DirectoryHelper
import com.remcoil.domain.useCase.files.OperationLogger
import com.remcoil.domain.useCase.files.OperationLoggerImpl
import com.remcoil.domain.useCase.files.SingleDirectoryHelper
import com.remcoil.domain.useCase.log.LogMessageGenerator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.logsComponents() {
    bind<JobLogsService>() with singleton { JobLogsService(instance(tag = "job_log"), instance(), instance()) }

    bind<MainLogsService>() with singleton { MainLogsService(instance(tag = "server_log")) }

    bind<OperationLogger>() with singleton { OperationLoggerImpl(instance()) }

    bind<DirectoryHelper>(tag = "job_log") with singleton {
        val path = Paths.get(instance<LogFileConfig>().jobLogFolder)
        SingleDirectoryHelper(path)
    }

    bind<DirectoryHelper>(tag = "server_log") with singleton {
        val path = Paths.get(instance<LogFileConfig>().serverLogFolder)
        SingleDirectoryHelper(path)
    }

    bind<LogMessageGenerator>() with singleton { LogMessageGenerator(instance()) }
}