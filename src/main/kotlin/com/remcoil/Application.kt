package com.remcoil

import com.remcoil.config.AppConfig
import com.remcoil.database.migrate
import com.remcoil.employees.employeeModule
import com.remcoil.employees.employeesComponents
import com.remcoil.tasks.tasksComponents
import com.remcoil.tasks.tasksModule
import io.ktor.application.*
import com.remcoil.tasks.tasksModuleOld
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    migrate(config.database)

    val engine = embeddedServer(Netty, port = config.http.port) {
        di {
            coreComponents(config)
            tasksComponents()
            employeesComponents()
        }

        main()
        configureSerialization()
        modules()
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

private fun Application.modules() {
    tasksModule()
    tasksModuleOld()

    employeeModule()
}

private fun DI.Builder.coreComponents(config: AppConfig) {
    bind<AppConfig>() with singleton { config }

    bind<Database>() with singleton {
        Database.connect(
            url = config.database.url,
            user = config.database.user,
            password = config.database.password
        )
    }
}
