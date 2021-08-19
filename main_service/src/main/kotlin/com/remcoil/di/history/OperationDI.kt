package com.remcoil.di.history

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.data.model.operation.OperationWithData
import com.remcoil.domain.files.DirectoryViewer
import com.remcoil.domain.files.OperationSaver
import com.remcoil.domain.files.OperationSaverImpl
import com.remcoil.domain.files.SingleDirectoryViewer
import com.remcoil.domain.message.MessageGenerator
import com.remcoil.domain.message.MessageUseCase
import com.remcoil.domain.message.SlotOpenMessageGenerator
import com.remcoil.domain.useCase.OperationUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.operationsComponents() {
    bind<DirectoryViewer>(tag = "histories") with singleton {
        val path = Paths.get(instance<LogFileConfig>().operationHistoryFolder)
        SingleDirectoryViewer("Отдали весь список файлов логов операций с ящиками", path)
    }

    bind<OperationSaver>() with singleton { OperationSaverImpl(instance()) }

    bind<MessageGenerator<OperationWithData.SlotOpened>>(tag = "slot_opened") with singleton {
        SlotOpenMessageGenerator(instance())
    }

    bind<MessageUseCase>() with singleton {
        val generators = listOf<MessageGenerator<*>>(instance(tag = "slot_opened"))
        MessageUseCase(generators)
    }

    bind<OperationUseCase>() with singleton { OperationUseCase(instance(), instance(), instance(), instance()) }
}