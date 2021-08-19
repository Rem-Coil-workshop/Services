package com.remcoil.data.database.employee

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.task.Task
import com.remcoil.data.exception.employee.NoSuchPermissionException
import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.*

class PermissionsDao(private val database: Database) {
    suspend fun getPermittedTaskByEmployeeId(employeeId: Int): List<Int> = safetySuspendTransaction(database) {
        EmployeePermissions
            .select { EmployeePermissions.employeeId eq employeeId }
            .map { row -> row[EmployeePermissions.taskId].value }
    }

    suspend fun getEmployeesByTask(taskId: Int): List<Int> = safetySuspendTransaction(database) {
        EmployeePermissions
            .select { EmployeePermissions.taskId eq taskId }
            .map { row -> row[EmployeePermissions.employeeId].value }
    }

    suspend fun addPermission(employee: Employee, task: Task) = safetySuspendTransaction(
        database,
        "Данное разрешение уже существует или не существует такого сотрудника или задачи"
    ) {
        EmployeePermissions.insert {
            it[employeeId] = employee.id
            it[taskId] = task.id
        }
    }

    suspend fun deletePermission(employee: Employee, task: Task) = safetySuspendTransaction(database) {
        val resultCode = EmployeePermissions.deleteWhere {
            EmployeePermissions.employeeId eq employee.id and (EmployeePermissions.taskId eq task.id)
        }
        if (resultCode == 0) throw NoSuchPermissionException("Не существует такого разрешения")
    }
}