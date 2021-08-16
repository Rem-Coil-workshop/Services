package com.remcoil.config.application

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.configureCORS() {
    install(CORS) {
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
        allowNonSimpleContentTypes = true
        allowCredentials = true
    }
}