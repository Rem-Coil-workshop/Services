package com.remcoil.employees

import com.remcoil.utils.logger

class EmployeesService(private val dao: EmployeesDao) {
    suspend fun getAll(): List<Employee> {
        val employees = dao.getAll()
        logger.info("Отдано ${employees.size} сотрудников")
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

    suspend fun addEmployee(employee: Employee): Employee {
        val newEmployee = dao.addEmployee(employee)
        logger.info("Создан рабочий ${newEmployee.employeeNumber}")
        return newEmployee
    }

    suspend fun removeById(id: Int) {
        dao.removeEmployeeById(id)
        logger.info("Удалён рабочий $id")
    }

    suspend fun removeByEmployeeNumber(number: Int) {
        dao.removeEmployeeByEmployeeNumber(number)
        logger.info("Удалён рабочий $number")
    }

    suspend fun checkByCard(card: Int) {
        getByEmployeeNumber(card)
        logger.info("Рабочий $card существует")
    }
}