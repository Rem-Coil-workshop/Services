package com.remcoil.endpoins.log

import com.remcoil.gateway.service.history.OperationsHistoryService
import com.remcoil.gateway.service.log.LogsService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.logsModule() {
    val operationsHistoryService: OperationsHistoryService by closestDI().instance()
    val logsService: LogsService by closestDI().instance()

    routing {
        authenticate("employee") {
            static("history") {
                files("operation_history")
            }

            get("/v1/history") {
                val files = operationsHistoryService.getAllFiles()
                call.respond(files)
            }
        }

        authenticate("admin") {
            static("logs") {
                files("logs/archive")
                default("logs/log.log")
            }

            get("/v1/logs") {
                val files = logsService.getAllLogFiles()
                call.respond(files)
            }
        }
    }
}

