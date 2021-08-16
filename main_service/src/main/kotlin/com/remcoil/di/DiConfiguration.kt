package com.remcoil.di

import com.remcoil.di.box.boxesComponents
import com.remcoil.config.hocon.AppConfig
import com.remcoil.config.hocon.LogFileConfig
import com.remcoil.config.hocon.RoutesConfig
import com.remcoil.config.hocon.SecureConfig
import com.remcoil.di.employee.employeesComponents
import com.remcoil.di.log.logsComponents
import com.remcoil.di.slot.slotsComponent
import com.remcoil.di.task.tasksComponents
import com.remcoil.di.user.rolesComponent
import com.remcoil.di.user.usersComponent
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.eagerSingleton
import org.kodein.di.ktor.di

fun Application.diComponents(config: AppConfig) {
    di {
        coreComponents(config)
        tasksComponents()
        employeesComponents()
        logsComponents()
        boxesComponents()
        slotsComponent()
        rolesComponent()
        usersComponent()
    }
}

private fun DI.Builder.coreComponents(config: AppConfig) {
    bind<AppConfig>() with eagerSingleton { config }

    bind<RoutesConfig>() with eagerSingleton { config.routes }

    bind<SecureConfig>() with eagerSingleton { config.secure }

    bind<LogFileConfig>() with eagerSingleton { config.logSource }

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