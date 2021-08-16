package com.remcoil.di.log

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.domain.files.DirectoryHelper
import com.remcoil.domain.files.SingleDirectoryHelper
import com.remcoil.gateway.service.log.LogsService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.logsComponents() {
    bind<DirectoryHelper>(tag = "logs") with singleton {
        val path = Paths.get(instance<LogFileConfig>().logsFolder)
        SingleDirectoryHelper(path)
    }

    bind<LogsService>() with singleton { LogsService(instance(tag = "logs")) }
}