package com.remcoil

import com.remcoil.config.application.configureCORS
import com.remcoil.config.application.configureMetrics
import com.remcoil.config.application.configureSerialization
import com.remcoil.config.application.configureWebSockets
import com.remcoil.config.hocon.AppConfig
import com.remcoil.data.database.migrate
import com.remcoil.di.diComponents
import com.remcoil.presentation.web.modules
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    migrate(config.database)

    val engine = embeddedServer(Netty, port = config.http.port) {
        main()
        modules()
        diComponents(config)
    }

    engine.start(wait = true)
}

private fun Application.main() {
    configureSerialization()
    configureCORS()
    configureWebSockets()
    configureMetrics()
}