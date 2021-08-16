package com.remcoil.di.history

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.domain.files.DirectoryHelper
import com.remcoil.domain.files.OperationHistorySaver
import com.remcoil.domain.files.OperationHistorySaverImpl
import com.remcoil.domain.files.SingleDirectoryHelper
import com.remcoil.domain.log.MessageGenerator
import com.remcoil.gateway.service.history.OperationsHistoryService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.operationsHistoryComponents() {
    bind<DirectoryHelper>(tag = "histories") with singleton {
        val path = Paths.get(instance<LogFileConfig>().operationHistoryFolder)
        SingleDirectoryHelper(path)
    }

    bind<OperationHistorySaver>() with singleton { OperationHistorySaverImpl(instance()) }

    bind<MessageGenerator>() with singleton { MessageGenerator(instance()) }

    bind<OperationsHistoryService>() with singleton {
        OperationsHistoryService(
            instance(tag = "histories"),
            instance(),
            instance()
        )
    }
}