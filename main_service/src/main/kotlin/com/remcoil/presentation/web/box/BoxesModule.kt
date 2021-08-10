package com.remcoil.presentation.web.box

import com.remcoil.data.model.box.Box
import com.remcoil.domain.service.box.BoxesService
import com.remcoil.utils.safetyReceiveWithBody
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
                call.safetyReceiveWithBody<Box> { box ->
                    call.respond(service.createBox(box))
                }
            }

            put {
                call.safetyReceiveWithBody<Box> { box ->
                    call.respond(service.updateBox(box))
                }
            }
        }
    }
}