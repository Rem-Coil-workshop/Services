package com.remcoil

import com.remcoil.boxes.boxesComponents
import com.remcoil.boxes.boxesModule
import com.remcoil.config.AppConfig
import com.remcoil.config.RoutesConfig
import com.remcoil.database.migrate
import com.remcoil.employees.employeeModule
import com.remcoil.employees.employeesComponents
import com.remcoil.logs.logsComponents
import com.remcoil.logs.logsModule
import com.remcoil.slots.slotModule
import com.remcoil.slots.slotsComponent
import com.remcoil.tasks.tasksComponents
import com.remcoil.tasks.tasksModule
import com.remcoil.tasks.tasksModuleOld
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.eagerSingleton
import org.kodein.di.ktor.di
import java.time.Duration

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    migrate(config.database)

    val engine = embeddedServer(Netty, port = config.http.port) {
        main()
        configureSerialization()
        configureMetrics()
        modules()
        diComponents(config)
    }

    engine.start(wait = true)
}

private fun Application.main() {
    install(CORS) {
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
    }
}

private fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
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

private fun Application.diComponents(config: AppConfig) {
    di {
        coreComponents(config)
        tasksComponents()
        employeesComponents()
        logsComponents()
        boxesComponents()
        slotsComponent()
    }
}

private fun Application.modules() {
    tasksModule()
    tasksModuleOld()

    employeeModule()
    logsModule()
    boxesModule()
    slotModule()
}

private fun DI.Builder.coreComponents(config: AppConfig) {
    bind<AppConfig>() with eagerSingleton { config }

    bind<RoutesConfig>() with eagerSingleton { config.routes }

    bind<Database>() with eagerSingleton {
        Database.connect(
            url = config.database.url,
            user = config.database.user,
            password = config.database.password
        )
    }

    bind<HttpClient>() with eagerSingleton {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }
}
