package com.remcoil.gateway.service.employee

import com.remcoil.data.database.employee.EmployeesDao
import com.remcoil.data.model.employee.Employee
import com.remcoil.utils.logger

class EmployeesService(private val dao: EmployeesDao) {
    suspend fun getAll(): List<Employee> {
        val employees = dao.getAll()
        logger.info("Отдано ${employees.size} сотрудников")
        return employees
    }

    suspend fun getByIds(ids: List<Int>): List<Employee> {
        val employees = dao.getEmployeesByIds(ids)
        logger.info("Отдали ${employees.size} сотрудников")
        return employees
    }

    suspend fun getById(id: Int): Employee {
        val employee = dao.getEmployeeById(id)
        logger.info("Отдан рабочий ${employee.employeeNumber}")
        return employee
    }

    suspend fun getByEmployeeNumber(number: Int): Employee {
        val employee = dao.getEmployeeByNumber(number)
        logger.info("Отдан рабочий ${employee.employeeNumber}")
        return employee
    }

    suspend fun add(employee: Employee): Employee {
        val newEmployee = dao.addEmployee(employee)
        logger.info("Создан рабочий ${newEmployee.employeeNumber}")
        return newEmployee
    }

    suspend fun remove(id: Int) {
        dao.removeEmployeeById(id)
        logger.info("Удалён рабочий $id")
    }

    suspend fun checkByCard(card: Int) {
        getByEmployeeNumber(card)
        logger.info("Рабочий $card существует")
    }
}