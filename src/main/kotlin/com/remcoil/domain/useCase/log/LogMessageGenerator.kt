package com.remcoil.domain.useCase.log

import com.remcoil.data.model.employee.Employee
import com.remcoil.domain.service.employee.EmployeesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LogMessageGenerator(
    private val employeesService: EmployeesService,
) {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    suspend fun generate(qrCode: String, card: Int): String = withContext(Dispatchers.IO) {
        val employee = async { employeesService.getByEmployeeNumber(card) }
        return@withContext generateByEmployeeAndTask(employee.await(), qrCode)
    }

    private fun generateByEmployeeAndTask(employee: Employee, task: String): String {
        val time = LocalTime.now()
        return "[${time.format(formatter)}] Рабочий ${employee.surname} ${employee.name} взял материалы для задачи $task"
    }
}