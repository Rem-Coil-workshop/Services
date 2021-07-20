package com.remcoil

import com.remcoil.config.AppConfig
import com.remcoil.data.database.migrate
import com.remcoil.di.diComponents
import com.remcoil.presentation.module.modules
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import java.time.Duration

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

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

private fun Application.configureCORS() {
    install(CORS) {
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
        allowNonSimpleContentTypes = true
        allowCredentials = true
    }
}

private fun Application.configureWebSockets() {
    install(WebSockets)
}

private fun Application.configureMetrics() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    appMicrometerRegistry.scrape(TextFormat.CONTENT_TYPE_OPENMETRICS_100)


    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        distributionStatisticConfig = DistributionStatisticConfig.Builder()
            .percentilesHistogram(true)
            .maximumExpectedValue(Duration.ofSeconds(20).toNanos().toDouble())
            .serviceLevelObjectives(
                Duration.ofMillis(100).toNanos().toDouble(),
                Duration.ofMillis(500).toNanos().toDouble()
            )
            .build()
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
    }

    routing {
        get("/metrics") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
