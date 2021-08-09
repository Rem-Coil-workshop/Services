package com.remcoil.domain.useCase.files

import java.nio.file.Files
import java.nio.file.Path

class SingleDirectoryHelper(private val rootDirectory: Path) : DirectoryHelper {
    override fun getAllFiles(): List<String> {
        val directory = openDirectory()
        val files = getAllPaths(directory)
        return files.map { path -> path.fileName.toString() }
    }

    private fun getAllPaths(directory: Path): List<Path> {
        val visitor = FileVisitor()
        Files.walkFileTree(directory, visitor)
        return visitor.getFiles()
    }

    private fun openDirectory(): Path {
        if (!Files.exists(rootDirectory)) {
            Files.createDirectory(rootDirectory)
        }
        return rootDirectory
    }
}