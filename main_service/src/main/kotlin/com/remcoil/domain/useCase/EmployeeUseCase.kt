package com.remcoil.domain.useCase

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.repository.EmployeeRepository
import com.remcoil.data.repository.PermissionRepository
import com.remcoil.utils.logged

class EmployeeUseCase(
    private val employeeRepository: EmployeeRepository,
    private val permissionRepository: PermissionRepository,
) : BaseUseCase() {
    suspend fun getAll(): List<Employee> = employeeRepository.getAll()

    suspend fun getById(id: Int): Employee = employeeRepository.getById(id)

    suspend fun getByNumber(number: Int): Employee = employeeRepository.getByNumber(number)

    suspend fun add(employee: Employee): Employee = employeeRepository.add(employee)

    suspend fun delete(id: Int) {
        permissionRepository.deleteByEmployee(id)
        employeeRepository.delete(id)
    }

    suspend fun checkByCard(card: Int) = logged("Рабочий $card существует") {
        employeeRepository.getByNumber(card)
    }
}