package com.remcoil.gateway.service.employee

import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.data.model.task.Task
import com.remcoil.gateway.service.task.TasksService
import com.remcoil.utils.logger

class PermissionsService(
    private val permissionDao: PermissionsDao,
    private val tasksService: TasksService,
    private val employeesService: EmployeesService
) {
    suspend fun getPermittedTasks(employeeId: Int): List<Task> {
        val tasksId = permissionDao.getPermittedTaskByEmployeeId(employeeId)
        logger.info("Отдали все задачи, которые разрешены для сотрудника $employeeId")
        return tasksService.getByIds(tasksId)
    }

    suspend fun getPermittedEmployees(taskId: Int): List<Employee> {
        val employeesId = permissionDao.getEmployeesByTask(taskId)
        logger.info("Отдали всех сотрудников, которые допущены до задачи с id = $taskId")
        return employeesService.getByIds(employeesId)
    }

    suspend fun add(permission: Permission) {
        permissionDao.addPermission(permission.employee, permission.task)
        logger.info("Добавили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}")
    }

    suspend fun delete(permission: Permission) {
        permissionDao.removePermission(permission.employee, permission.task)
        logger.info("Удалили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}")
    }
}