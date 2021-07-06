package com.remcoil.logs

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.logsModule() {
    val service: LogsService by closestDI().instance()

    routing {
        route("/v1/logs") {
            get("/{page}") {
                call.safetyReceive("page") { page ->
                    call.respond(service.getPage(page.toInt()))
                }
            }

            post {
                call.safetyReceive<LogData> { logData ->
                    call.respond(service.create(logData.qrCode, logData.cardCode))
                }
            }
        }
    }
}

@Serializable
data class LogData(val qrCode: String, val cardCode: Int)