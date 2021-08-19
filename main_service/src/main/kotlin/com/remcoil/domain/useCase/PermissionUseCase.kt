package com.remcoil.domain.useCase

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.data.model.task.Task
import com.remcoil.data.repository.EmployeeRepository
import com.remcoil.data.repository.PermissionRepository
import com.remcoil.data.repository.TaskRepository

class PermissionUseCase(
    private val employeeRepository: EmployeeRepository,
    private val permissionRepository: PermissionRepository,
    private val taskRepository: TaskRepository,
) {
    suspend fun getPermittedTasks(employeeId: Int): List<Task> {
        val ids = permissionRepository.getPermittedTasksIds(employeeId)
        return taskRepository.getByIds(ids)
    }

    suspend fun getPermittedEmployees(taskId: Int): List<Employee> {
        val employeesId = permissionRepository.getPermittedEmployeesIds(taskId)
        return employeeRepository.getByIds(employeesId)
    }

    suspend fun add(permission: Permission) = permissionRepository.add(permission)

    suspend fun delete(permission: Permission) = permissionRepository.delete(permission)
}