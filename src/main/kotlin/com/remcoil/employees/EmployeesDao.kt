package com.remcoil.employees

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class EmployeesDao(private val database: Database) {
    fun getEmployeeById(id: Int): EmployeeWithId = transaction(database) {
        Employees
            .select { Employees.id eq id }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException()
    }

    fun getEmployeeByNumber(number: Int): EmployeeWithId = transaction(database) {
        Employees
            .select { Employees.employeeNumber eq number }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException()
    }


    fun addEmployee(employee: Employee): EmployeeWithId = transaction(database) {
        val id = Employees.insertAndGetId {
            it[employeeNumber] = employee.employeeNumber
            it[name] = employee.name
            it[surname] = employee.surname
        }

        EmployeeWithId(id.value, employee)
    }

    fun removeEmployeeById(id: Int) = transaction(database) {
        val resultCode = Employees.deleteWhere { Employees.id eq id }
        if (resultCode == 0) throw NoSuchEmployeeException()
    }

    fun removeEmployeeByEmployeeNumber(number: Int) = transaction(database) {
        val resultCode = Employees.deleteWhere { Employees.employeeNumber eq number }
        if (resultCode == 0) throw NoSuchEmployeeException()
    }

    private fun extractEmployee(row: ResultRow): EmployeeWithId = EmployeeWithId(
        row[Employees.id].value,
        row[Employees.employeeNumber],
        row[Employees.name],
        row[Employees.surname],
    )
}