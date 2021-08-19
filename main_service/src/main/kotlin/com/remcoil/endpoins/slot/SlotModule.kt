package com.remcoil.endpoins.slot

import com.remcoil.data.model.slot.Slot
import com.remcoil.domain.device.SlotOpener
import com.remcoil.domain.useCase.SlotUseCase
import com.remcoil.utils.safetyReceiveWithBody
import com.remcoil.utils.safetyReceiveWithRouteParameter
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotModule() {
    val slotUseCase: SlotUseCase by closestDI().instance()

    routing {
        route("/v1/slots") {
            authenticate("employee") {
                get {
                    call.respond(slotUseCase.getAll())
                }

                post {
                    call.safetyReceiveWithBody<Slot> { box ->
                        call.respond(slotUseCase.create(box))
                    }
                }

                put {
                    call.safetyReceiveWithBody<Slot> { box ->
                        call.respond(slotUseCase.update(box))
                    }
                }

                get("/open/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        slotUseCase.open(id.toInt())
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }
        }
    }
}