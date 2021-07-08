package com.remcoil.employees

import com.remcoil.utils.safetyTransaction
import org.jetbrains.exposed.sql.*

class EmployeesDao(private val database: Database) {
    fun getAll(): List<EmployeeWithId> = safetyTransaction(database) {
        Employees
            .selectAll()
            .map(::extractEmployee)
    }

    fun getEmployeeById(id: Int): EmployeeWithId = safetyTransaction(database) {
        Employees
            .select { Employees.id eq id }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException("Сотрудника с таким id не существует")
    }

    fun getEmployeeByNumber(number: Int): EmployeeWithId = safetyTransaction(database) {
        Employees
            .select { Employees.employeeNumber eq number }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException("Сотрудника с таким номером не существует")
    }


    fun addEmployee(employee: Employee): EmployeeWithId =
        safetyTransaction(database, "Введено не уникальное значение номера сотрудника") {
            val id = Employees.insertAndGetId {
                it[employeeNumber] = employee.employeeNumber
                it[name] = employee.name
                it[surname] = employee.surname
            }

            EmployeeWithId(id.value, employee)
        }

    fun removeEmployeeById(id: Int) = safetyTransaction(database) {
        val resultCode = Employees.deleteWhere { Employees.id eq id }
        if (resultCode == 0) throw NoSuchEmployeeException("Сотрудника с таким id не существует")
    }

    fun removeEmployeeByEmployeeNumber(number: Int) = safetyTransaction(database) {
        val resultCode = Employees.deleteWhere { Employees.employeeNumber eq number }
        if (resultCode == 0) throw NoSuchEmployeeException("Сотрудника с таким номером не существует")
    }

    private fun extractEmployee(row: ResultRow): EmployeeWithId = EmployeeWithId(
        row[Employees.id].value,
        row[Employees.employeeNumber],
        row[Employees.name],
        row[Employees.surname],
    )
}