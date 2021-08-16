package com.remcoil.config.application

import io.ktor.application.*
import io.ktor.websocket.*

fun Application.configureWebSockets() {
    install(WebSockets)
}