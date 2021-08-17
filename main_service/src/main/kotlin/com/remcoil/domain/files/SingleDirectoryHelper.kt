package com.remcoil.domain.files

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path

class SingleDirectoryHelper(private val rootDirectory: Path) : DirectoryHelper {
    override suspend fun getAllFiles(): List<String> = withContext(Dispatchers.IO) {
        val directory = openDirectory()
        val files = getAllPaths(directory)
        return@withContext files.map { path -> path.fileName.toString() }
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