package com.remcoil.employees

import com.remcoil.utils.safetySuspendTransaction
import org.jetbrains.exposed.sql.*

class EmployeesDao(private val database: Database) {
    suspend fun getAll(): List<EmployeeWithId> = safetySuspendTransaction(database) {
        Employees
            .selectAll()
            .map(::extractEmployee)
    }

    suspend fun getEmployeeById(id: Int): EmployeeWithId = safetySuspendTransaction(database) {
        Employees
            .select { Employees.id eq id }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException("Сотрудника с таким id не существует")
    }

    suspend fun getEmployeeByNumber(number: Int): EmployeeWithId = safetySuspendTransaction(database) {
        Employees
            .select { Employees.employeeNumber eq number }
            .map(::extractEmployee)
            .firstOrNull()
            ?: throw NoSuchEmployeeException("Сотрудника с таким номером не существует")
    }


    suspend fun addEmployee(employee: Employee): EmployeeWithId =
        safetySuspendTransaction(database, "Введено не уникальное значение номера сотрудника") {
            val id = Employees.insertAndGetId {
                it[employeeNumber] = employee.employeeNumber
                it[name] = employee.name
                it[surname] = employee.surname
            }

            EmployeeWithId(id.value, employee)
        }

    suspend fun removeEmployeeById(id: Int) = safetySuspendTransaction(database) {
        val resultCode = Employees.deleteWhere { Employees.id eq id }
        if (resultCode == 0) throw NoSuchEmployeeException("Сотрудника с таким id не существует")
    }

    suspend fun removeEmployeeByEmployeeNumber(number: Int) = safetySuspendTransaction(database) {
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