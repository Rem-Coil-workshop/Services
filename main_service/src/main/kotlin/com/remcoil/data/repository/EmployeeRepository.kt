package com.remcoil.data.repository

import com.remcoil.data.database.employee.EmployeesDao
import com.remcoil.data.model.employee.Employee
import com.remcoil.utils.logged
import com.remcoil.utils.loggedEntity

class EmployeeRepository(private val dao: EmployeesDao) {
    suspend fun getAll(): List<Employee> = loggedEntity({ "Отдано ${it.size} сотрудников" }) {
        dao.getAll()
    }

    suspend fun getByIds(ids: List<Int>): List<Employee> = loggedEntity({ "Отдано ${it.size} сотрудников" }) {
        dao.getEmployeesByIds(ids)
    }

    suspend fun getById(id: Int): Employee = loggedEntity({ "Отдан рабочий ${it.employeeNumber}" }) {
        dao.getEmployeeById(id)
    }

    suspend fun getByNumber(number: Int): Employee = loggedEntity({ "Отдан рабочий ${it.employeeNumber}" }) {
        dao.getEmployeeByNumber(number)
    }

    suspend fun add(employee: Employee): Employee = loggedEntity({ "Создан рабочий ${it.employeeNumber}" }) {
        dao.addEmployee(employee)
    }

    suspend fun remove(id: Int) = logged("Удалён рабочий $id") {
        dao.removeEmployeeById(id)
    }
}