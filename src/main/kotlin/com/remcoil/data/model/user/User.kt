package com.remcoil.data.model.user

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
    @Transient
    val id: Int? = null,
    val firstname: String,
    val lastname: String,
    @Transient
    val password: String = "",
    val role: String,
) {
    constructor(credentials: UserCredentials) : this(
        null,
        credentials.firstname,
        credentials.lastname,
        credentials.password,
        credentials.role.uppercase()
    )
}
