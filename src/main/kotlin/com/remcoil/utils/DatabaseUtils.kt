package com.remcoil.utils

import com.remcoil.base.DatabaseException
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

fun <T> safetyTransaction(
    db: Database,
    errorMessage: String? = "Ошибка при выполнении запроса в базу данных",
    statement: Transaction.() -> T
): T {
    try {
        return transaction(db, statement)
    } catch (e: ExposedSQLException) {
        throw DatabaseException(errorMessage)
    }
}