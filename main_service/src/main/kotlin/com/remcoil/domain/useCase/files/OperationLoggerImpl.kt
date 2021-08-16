package com.remcoil.domain.useCase.files

import com.remcoil.config.hocon.LogFileConfig
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OperationLoggerImpl(private val config: LogFileConfig) : OperationLogger {
    private val directory = Paths.get(config.jobLogFolder)
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    override fun log(message: String) {
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
        return config.baseFileName + textDate + config.fileExtension
    }

    private fun openDirectory(): Path {
        if (!Files.exists(directory)) Files.createDirectory(directory)
        return directory
    }
}