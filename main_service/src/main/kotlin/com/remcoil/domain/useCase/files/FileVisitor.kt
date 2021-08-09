package com.remcoil.domain.useCase.files

import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class FileVisitor : SimpleFileVisitor<Path>() {
    private val files = mutableListOf<Path>()

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        if (file != null) files.add(file)
        return FileVisitResult.CONTINUE
    }

    fun getFiles(): List<Path> = files
}