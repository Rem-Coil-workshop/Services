package com.remcoil.logs

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class LogsDao(private val database: Database) {
    fun getAllLogs(): List<LogFromDB> = transaction(database) {
        Logs
            .selectAll()
            .map(::extractLog)
    }

    private fun extractLog(row: ResultRow): LogFromDB = LogFromDB(
        row[Logs.id].value,
        row[Logs.employeeId].value,
        row[Logs.date],
        row[Logs.taskId].value
    )
}