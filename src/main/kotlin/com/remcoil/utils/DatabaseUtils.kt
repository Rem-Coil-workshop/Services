package com.remcoil.utils

import com.remcoil.base.DatabaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun <T> safetySuspendTransaction(
    db: Database,
    errorMessage: String? = "Ошибка при выполнении запроса в базу данных",
    statement: Transaction.() -> T
): T = withContext(Dispatchers.IO) {
    try {
        return@withContext transaction(db, statement)
    } catch (e: ExposedSQLException) {
        logger.debug("${e.message}")
        throw DatabaseException(errorMessage)
    }
}