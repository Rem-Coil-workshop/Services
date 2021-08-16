package com.remcoil.config.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.remcoil.config.hocon.SecureConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.configureSecurity(config: SecureConfig) {
    install(Authentication) {
        val jwtVerifier = JWT
            .require(Algorithm.HMAC256(config.secret))
            .build()

        jwt("admin") {
            verifier(jwtVerifier)
            validate { credential ->
                if (credential.payload.getClaim("role").asString().lowercase() == "admin") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        jwt("employee") {
            verifier(jwtVerifier)
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}