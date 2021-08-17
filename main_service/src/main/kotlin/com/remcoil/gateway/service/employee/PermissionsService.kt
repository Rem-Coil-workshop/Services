package com.remcoil.gateway.service.employee

import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.data.model.task.Task
import com.remcoil.gateway.service.task.TasksService
import com.remcoil.utils.logged

class PermissionsService(
    private val permissionDao: PermissionsDao,
    private val tasksService: TasksService,
    private val employeesService: EmployeesService
) {
    suspend fun getPermittedTasks(employeeId: Int): List<Task> =
        logged(message = "Отдали все задачи, которые разрешены для сотрудника $employeeId") {
            val tasksId = permissionDao.getPermittedTaskByEmployeeId(employeeId)
            return tasksService.getByIds(tasksId)
        }

    suspend fun getPermittedEmployees(taskId: Int): List<Employee> =
        logged("Отдали всех сотрудников, которые допущены до задачи с id = $taskId") {
            val employeesId = permissionDao.getEmployeesByTask(taskId)
            return employeesService.getByIds(employeesId)
        }

    suspend fun add(permission: Permission): Unit =
        logged("Добавили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}") {
            permissionDao.addPermission(permission.employee, permission.task)
        }

    suspend fun delete(permission: Permission): Unit =
        logged("Удалили разрешение на использование задачи ${permission.task.qrCode} для сотрудника ${permission.employee.fullName}") {
            permissionDao.removePermission(permission.employee, permission.task)
        }
}