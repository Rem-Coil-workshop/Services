package com.remcoil.di.log

import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.domain.files.DirectoryViewer
import com.remcoil.domain.files.SingleDirectoryViewer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import java.nio.file.Paths

fun DI.Builder.logsComponents() {
    bind<DirectoryViewer>(tag = "logs") with singleton {
        val path = Paths.get(instance<LogFileConfig>().logsFolder)
        SingleDirectoryViewer("Отдали весь список файлов логов системы", path)
    }
}