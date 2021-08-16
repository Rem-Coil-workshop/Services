package com.remcoil.data.model.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.remcoil.config.hocon.SecureConfig
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Token(val token: String) {
    companion object {
        operator fun invoke(user: User, secureConfig: SecureConfig): Token {
            val token = JWT
                .create()
                .withClaim("firstname", user.firstname)
                .withClaim("lastname", user.lastname)
                .withClaim("role", user.role)
                .withExpiresAt(Date(System.currentTimeMillis() + secureConfig.time))
                .sign(Algorithm.HMAC256(secureConfig.secret))

            return Token(token)
        }
    }
}