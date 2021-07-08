package com.remcoil.employees

class EmployeesService(private val dao: EmployeesDao) {
    fun getAll() = dao.getAll()

    fun getById(id: Int) = dao.getEmployeeById(id)

    fun getByEmployeeNumber(number: Int): EmployeeWithId = dao.getEmployeeByNumber(number)

    fun addEmployee(employee: Employee): EmployeeWithId = dao.addEmployee(employee)

    fun removeById(id: Int) = dao.removeEmployeeById(id)

    fun removeByEmployeeNumber(number: Int) = dao.removeEmployeeByEmployeeNumber(number)

    fun checkByCard(card: Int) {
        getByEmployeeNumber(card)
    }
}