package com.remcoil.domain.files

interface DirectoryHelper {
    suspend fun getAllFiles(): List<String>
}