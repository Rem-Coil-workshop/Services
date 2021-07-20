package com.remcoil.data.database.log

import com.remcoil.exception.log.OutOfLogsException
import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.log.Log
import com.remcoil.data.model.log.LogFromDatabase
import com.remcoil.data.model.task.Task
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class LogsDao(private val database: Database) {
    companion object {
        const val DEFAULT_COUNT_VALUE = -1L
        const val COUNT_LOGS_ON_PAGE = 20L
    }

    private var countLogs = DEFAULT_COUNT_VALUE

    suspend fun getPage(page: Int): List<LogFromDatabase> {
        updateCache()
        if (page.firstElement() > countLogs) throw OutOfLogsException("$countLogs")

        return safetySuspendTransaction(database) {
            Logs
                .selectAll()
                .limit(COUNT_LOGS_ON_PAGE.toInt(), offset = page.firstElement())
                .map(::extractLog)
        }
    }

    private fun Int.firstElement(): Long {
        return (this - 1) * COUNT_LOGS_ON_PAGE
    }

    private fun updateCache() {
        if (countLogs == DEFAULT_COUNT_VALUE) {
            transaction(database) {
                countLogs = Logs.selectAll().count()
            }
        }
    }

    suspend fun addLog(task: Task, employee: Employee): Log =
        safetySuspendTransaction(database, "Ошибка в указании рабочего или задачи, возможно их они не существуют.") {
            val time = LocalDateTime.now()
            val id = Logs.insertAndGetId {
                it[employeeId] = employee.id
                it[date] = time
                it[taskId] = task.id
            }

            incrementCountLogs()
            Log(id.value, employee.name, employee.surname, time.toString(), task.qrCode)
        }

    private fun incrementCountLogs() {
        updateCache()
        countLogs++
    }

    private fun extractLog(row: ResultRow): LogFromDatabase = LogFromDatabase(
        row[Logs.id].value,
        row[Logs.employeeId].value,
        row[Logs.date],
        row[Logs.taskId].value
    )
}