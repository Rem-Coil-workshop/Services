package com.remcoil.di.operation

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.domain.files.DirectoryViewer
import com.remcoil.domain.files.OperationSaver
import com.remcoil.domain.files.OperationSaverImpl
import com.remcoil.domain.files.SingleDirectoryViewer
import com.remcoil.domain.message.*
import com.remcoil.domain.useCase.OperationUseCase
import org.kodein.di.*
import java.nio.file.Paths

fun DI.Builder.operationsComponents() {
    bind<DirectoryViewer>(tag = "histories") with singleton {
        val path = Paths.get(instance<LogFileConfig>().operationHistoryFolder)
        SingleDirectoryViewer("Отдали весь список файлов логов операций с ящиками", path)
    }

    bind<OperationSaver>() with singleton { OperationSaverImpl(instance()) }

    bind<MessageGenerator<*>>(tag = "employee_slot_opened") with singleton {
        SlotOpenMessageGenerator(instance())
    }

    bind<MessageGenerator<*>>(tag = "user_slot_opened") with singleton {
        UserSlotOpenMessageGenerator(instance())
    }

    bind<MessageGenerator<*>>(tag = "user_slot_update") with singleton {
        UserSlotUpdateMessageGenerator(instance())
    }

    bind<MessageGenerator<*>>(tag = "user_change_permission") with singleton {
        UserPermissionMessageGenerator(instance())
    }

    bind<MessageUseCase>() with singleton {
        val generators = listOf<MessageGenerator<*>>(
            instance(tag = "employee_slot_opened"),
            instance(tag = "user_slot_opened"),
            instance(tag = "user_slot_update"),
            instance(tag = "user_change_permission"),
        )
        MessageUseCase(generators)
    }

    bind<OperationUseCase>() with eagerSingleton {
        OperationUseCase(
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
}