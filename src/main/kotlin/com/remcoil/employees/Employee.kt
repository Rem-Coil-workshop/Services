package com.remcoil.employees

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Int? = null,
    val employeeNumber: Int,
    val name: String,
    val surname: String
)