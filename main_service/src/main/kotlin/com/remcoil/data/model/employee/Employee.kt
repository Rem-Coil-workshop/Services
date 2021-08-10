package com.remcoil.data.model.employee

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val id: Int? = null,
    val employeeNumber: Int,
    val name: String,
    val surname: String
) {
    val fullName get() = "$name $surname"
}