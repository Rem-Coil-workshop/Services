package com.remcoil.employees

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeWithId(
    val id: Int,
    val employeeNumber: Int,
    val name: String,
    val surname: String
) {
    constructor(id: Int, employee: Employee) : this(
        id,
        employee.employeeNumber,
        employee.name,
        employee.surname
    )
}
