package com.remcoil.data.database.employee

import com.remcoil.data.database.task.Tasks
import org.jetbrains.exposed.sql.Table

object EmployeePermissions : Table(name = "EMPLOYEE_PERMISSIONS") {
    val employeeId = reference("EMPLOYEE_ID", Employees)
    val taskId = reference("TASK_ID", Tasks)
    override val primaryKey = PrimaryKey(employeeId, taskId)
}