package com.remcoil.di.history

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.domain.files.DirectoryViewer
import com.remcoil.domain.files.OperationHistorySaver
import com.remcoil.domain.files.OperationHistorySaverImpl
import com.remcoil.domain.files.SingleDirectoryViewer
import com.remcoil.domain.message.MessageGenerator
import com.remcoil.gateway.service.history.SlotOperationsHistoryService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.operationsHistoryComponents() {
    bind<DirectoryViewer>(tag = "histories") with singleton {
        val path = Paths.get(instance<LogFileConfig>().operationHistoryFolder)
        SingleDirectoryViewer("Отдали весь список файлов логов операций с ящиками", path)
    }

    bind<OperationHistorySaver>() with singleton { OperationHistorySaverImpl(instance()) }

//    bind<MessageGenerator>() with singleton { MessageGenerator(instance()) }

    bind<SlotOperationsHistoryService>() with singleton {
        SlotOperationsHistoryService(instance(), instance())
    }
}