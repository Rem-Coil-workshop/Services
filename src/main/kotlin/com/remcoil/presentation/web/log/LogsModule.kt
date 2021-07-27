package com.remcoil.presentation.web.log

import com.remcoil.data.model.log.LogData
import com.remcoil.domain.service.log.LogsService
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.logsModule() {
    val service: LogsService by closestDI().instance()

    routing {
        static("logs") {
            files("log")
        }

        route("/v1/logs") {
            get {
                val files = service.getAllFiles()
                call.respond(files)
            }

            post {
                call.safetyReceive<LogData> { logData ->
                    service.log(logData.qrCode, logData.cardCode)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

