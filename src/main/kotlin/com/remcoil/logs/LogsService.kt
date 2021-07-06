package com.remcoil.logs

import com.remcoil.employees.EmployeesDao
import com.remcoil.tasks.TasksDao

class LogsService(
    private val logsDao: LogsDao,
    private val employeesDao: EmployeesDao,
    private val taskDao: TasksDao
) {

    fun create(qrCode: String, cardCode: Int): Log {
        val task = taskDao.getTaskByName(qrCode)
        val employee = employeesDao.getEmployeeByNumber(cardCode)
        return logsDao.addLog(task, employee)
    }

    fun getPage(page: Int): List<Log> {
        val logs = logsDao.getPage(page)
        return logs.map { logFromDB ->
            val task = taskDao.getTaskById(logFromDB.taskId)
            val employee = employeesDao.getEmployeeById(logFromDB.employeeId)
            Log(logFromDB.id, employee.name, employee.surname, logFromDB.date.toString(), task.name)
        }
    }
}