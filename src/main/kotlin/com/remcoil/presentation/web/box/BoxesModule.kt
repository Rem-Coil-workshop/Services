package com.remcoil.presentation.web.box

import com.remcoil.data.model.box.Box
import com.remcoil.domain.service.box.BoxesService
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.boxesModule() {
    val service: BoxesService by closestDI().instance()

    routing {
        route("/v1/boxes") {
            get {
                call.respond(service.getAll())
            }

            post {
                call.safetyReceive<Box> { box ->
                    call.respond(service.createBox(box))
                }
            }

            put {
                call.safetyReceive<Box> { box ->
                    call.respond(service.updateBox(box))
                }
            }
        }
    }
}