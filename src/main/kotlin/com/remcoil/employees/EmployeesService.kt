package com.remcoil.employees

class EmployeesService(private val dao: EmployeesDao) {
    suspend fun getAll() = dao.getAll()

    suspend fun getById(id: Int) = dao.getEmployeeById(id)

    suspend fun getByEmployeeNumber(number: Int): EmployeeWithId = dao.getEmployeeByNumber(number)

    suspend fun addEmployee(employee: Employee): EmployeeWithId = dao.addEmployee(employee)

    suspend fun removeById(id: Int) = dao.removeEmployeeById(id)

    suspend fun removeByEmployeeNumber(number: Int) = dao.removeEmployeeByEmployeeNumber(number)

    suspend fun checkByCard(card: Int) {
        getByEmployeeNumber(card)
    }
}