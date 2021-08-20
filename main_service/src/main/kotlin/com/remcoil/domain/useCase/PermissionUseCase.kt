package com.remcoil.domain.useCase

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.data.model.operation.Operation
import com.remcoil.data.model.task.Task
import com.remcoil.data.model.user.User
import com.remcoil.data.repository.EmployeeRepository
import com.remcoil.data.repository.PermissionRepository
import com.remcoil.data.repository.TaskRepository

class PermissionUseCase(
    private val employeeRepository: EmployeeRepository,
    private val permissionRepository: PermissionRepository,
    private val taskRepository: TaskRepository,
) : BaseUseCase() {
    suspend fun getPermittedTasks(employeeId: Int): List<Task> {
        val ids = permissionRepository.getPermittedTasksIds(employeeId)
        return taskRepository.getByIds(ids)
    }

    suspend fun getPermittedEmployees(taskId: Int): List<Employee> {
        val employeesId = permissionRepository.getPermittedEmployeesIds(taskId)
        return employeeRepository.getByIds(employeesId)
    }

    suspend fun add(permission: Permission, user: User) {
        permissionRepository.add(permission)
        savePermissionOperation(permission, user, true)
    }

    suspend fun delete(permission: Permission, user: User) {
        permissionRepository.delete(permission)
        savePermissionOperation(permission, user, false)
    }

    private suspend fun savePermissionOperation(permission: Permission, user: User, isAdd: Boolean) {
        operationUseCase.saveOperation(
            Operation.UserChangePermission(
                user,
                permission.employee,
                permission.task,
                isAdd
            )
        )
    }
}