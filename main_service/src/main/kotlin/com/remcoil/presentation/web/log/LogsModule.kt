package com.remcoil.presentation.web.log

import com.remcoil.data.model.log.LogData
import com.remcoil.domain.service.log.JobLogsService
import com.remcoil.domain.service.log.MainLogsService
import com.remcoil.utils.safetyReceiveWithBody
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.logsModule() {
    val jobLogsService: JobLogsService by closestDI().instance()
    val mainLogsService: MainLogsService by closestDI().instance()

    routing {
        static("logs") {
            files("log")
        }

        static("server_log") {
            files("server_log/archive")
            default("server_log/log.log")
        }

        route("/v1/logs") {
            get("/job") {
                val files = jobLogsService.getAllFiles()
                call.respond(files)
            }

            get("/main") {
                val files = mainLogsService.getAllLogFiles()
                call.respond(files)
            }
        }
    }
}

