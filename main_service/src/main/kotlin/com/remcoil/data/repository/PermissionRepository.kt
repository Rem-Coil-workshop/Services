package com.remcoil.data.repository

import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.data.model.employee.Permission
import com.remcoil.utils.logged

class PermissionRepository(private val dao: PermissionsDao) {
    suspend fun getPermittedTasksIds(employeeId: Int): List<Int> =
        logged(message = "Отдали все задачи, которые разрешены для сотрудника $employeeId") {
            return dao.getPermittedTaskByEmployeeId(employeeId)
        }

    suspend fun getPermittedEmployeesIds(taskId: Int): List<Int> =
        logged("Отдали всех сотрудников, которые допущены до задачи с id = $taskId") {
            return dao.getEmployeesByTask(taskId)
        }

    suspend fun add(permission: Permission): Unit =
        logged("Добавили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}") {
            dao.addPermission(permission.employee, permission.task)
        }

    suspend fun delete(permission: Permission): Unit =
        logged("Удалили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}") {
            dao.deletePermission(permission.employee, permission.task)
        }
}