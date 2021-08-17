package com.remcoil.endpoins.log

import com.remcoil.domain.files.DirectoryViewer
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.logsModule() {
    val operations: DirectoryViewer by closestDI().instance(tag = "histories")
    val logs: DirectoryViewer by closestDI().instance(tag = "logs")

    routing {
        authenticate("employee") {
            static("history") {
                files("operation_history")
            }

            get("/v1/history") {
                call.respond(operations.view())
            }
        }

        authenticate("admin") {
            static("logs") {
                files("logs/archive")
                default("logs/log.log")
            }

            get("/v1/logs") {
                call.respond(logs.view())
            }
        }
    }
}

