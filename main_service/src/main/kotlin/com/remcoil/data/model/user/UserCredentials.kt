package com.remcoil.data.model.user

import kotlinx.serialization.Serializable

@Serializable
class UserCredentials(
    val firstname: String,
    val lastname: String,
    val password: String,
    val role: String = "employee",
)
