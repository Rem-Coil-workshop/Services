package com.remcoil.domain.files

import com.remcoil.config.hocon.LogFileConfig
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OperationHistorySaverImpl(private val config: LogFileConfig) : OperationHistorySaver {
    private val directory = Paths.get(config.operationHistoryFolder)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    override fun save(message: String) {
        val file = openFile()
        Files.newBufferedWriter(file, StandardOpenOption.APPEND).use { writer ->
            writer.write(message)
            writer.newLine()
        }
    }

    private fun openFile(): Path {
        val dir = openDirectory()
        val file = Paths.get(dir.toString(), generateFileName())
        if (!Files.exists(file)) Files.createFile(file)
        return file
     }

    private fun generateFileName(): String {
        val currentDate = LocalDate.now()
        val textDate = currentDate.format(formatter)
        return textDate + config.fileExtension
    }

    private fun openDirectory(): Path {
        if (!Files.exists(directory)) Files.createDirectory(directory)
        return directory
    }
}