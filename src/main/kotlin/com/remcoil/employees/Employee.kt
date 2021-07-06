package com.remcoil.employees

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val employeeNumber: Int,
    val name: String,
    val surname: String
)