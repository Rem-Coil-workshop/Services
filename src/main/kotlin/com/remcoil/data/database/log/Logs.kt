package com.remcoil.data.database.log

import com.remcoil.data.database.employee.Employees
import com.remcoil.data.database.task.Tasks
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Logs : IntIdTable() {
    val employeeId = reference("employee_id", Employees)
    val date = datetime("date")
    val taskId = reference("task_id", Tasks)
}