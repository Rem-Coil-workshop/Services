package com.remcoil.domain.useCase

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.repository.EmployeeRepository
import com.remcoil.utils.logged

class EmployeeUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun getAll(): List<Employee> = employeeRepository.getAll()

    suspend fun getByIds(ids: List<Int>): List<Employee> = employeeRepository.getByIds(ids)

    suspend fun getById(id: Int): Employee = employeeRepository.getById(id)

    suspend fun getByNumber(number: Int): Employee = employeeRepository.getByNumber(number)

    suspend fun add(employee: Employee): Employee = employeeRepository.add(employee)

    suspend fun remove(id: Int) = employeeRepository.remove(id)

    suspend fun checkByCard(card: Int) = logged("Рабочий $card существует") {
        employeeRepository.getByNumber(card)
    }
}