package com.remcoil.domain.message

import com.remcoil.data.model.employee.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MessageGenerator() {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

//    suspend fun generate(qrCode: String, card: Int): String = withContext(Dispatchers.IO) {
//        val employee = async { employeesService.getByEmployeeNumber(card) }
//        return@withContext generateByEmployeeAndTask(employee.await(), qrCode)
//    }

    private fun generateByEmployeeAndTask(employee: Employee, task: String): String {
        return generateMessage(
            entity = "рабочий ${employee.surname} ${employee.name}",
            action = "взял материалы для задачи $task"
        )
    }

    private fun generateMessage(entity: String, action: String): String {
        val time = LocalTime.now()
        return "[${time.format(formatter)}] $entity $action"
    }
}