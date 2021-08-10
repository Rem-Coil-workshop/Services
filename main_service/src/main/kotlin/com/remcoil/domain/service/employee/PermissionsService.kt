package com.remcoil.domain.service.employee

import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.utils.logger

class PermissionsService(private val permissionDao: PermissionsDao) {
    suspend fun getPermissions(employee: Employee): List<Int> {
        val tasks = permissionDao.getPermittedTaskByEmployee(employee)
        logger.info("Отдали все задачи, которые разрешены ${employee.fullName}")
        return tasks
    }

    suspend fun addPermission(permission: Permission) {
        permissionDao.addPermission(permission.employee, permission.task)
        logger.info("Добавили разрешение на использование ${permission.task.qrCode} для ${permission.employee.fullName}")
    }

    suspend fun deletePermission(permission: Permission) {
        permissionDao.removePermission(permission.employee, permission.task)
        logger.info("Удалили разрешение на использование ${permission.task.qrCode} для ${permission.employee.fullName}")
    }
}