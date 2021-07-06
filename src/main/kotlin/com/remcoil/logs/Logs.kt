package com.remcoil.logs

import com.remcoil.employees.Employees
import com.remcoil.tasks.Tasks
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object Logs : IntIdTable() {
    val employeeId = reference("employee_id", Employees)
    val date = datetime("date")
    val taskId = reference("task_id", Tasks)
}