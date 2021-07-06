package com.remcoil.employees

import org.jetbrains.exposed.dao.id.IntIdTable

object Employees : IntIdTable() {
    val employeeNumber = integer("employee_number")
    val name = varchar("name", 20)
    val surname = varchar("surname", 20)
}