package com.remcoil.endpoins.slot

import com.remcoil.data.model.slot.CardCode
import com.remcoil.data.model.slot.QrCode
import com.remcoil.data.model.slot.Slot
import com.remcoil.gateway.controller.slot.SlotStateController
import com.remcoil.domain.device.SlotOpener
import com.remcoil.gateway.service.slot.SlotsService
import com.remcoil.utils.logger
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
    val service: SlotsService by closestDI().instance()
    val stateController: SlotStateController by closestDI().instance()
    val opener: SlotOpener by closestDI().instance()

    routing {
        route("/v1/slots") {
            authenticate("employee") {
                get {
                    call.respond(service.getAll())
                }

                post {
                    call.safetyReceiveWithBody<Slot> { box ->
                        call.respond(service.create(box))
                    }
                }

                put {
                    call.safetyReceiveWithBody<Slot> { box ->
                        call.respond(service.update(box))
                    }
                }

                get("/open/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        opener.openByBoxNumber(id.toInt())
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }

            route("/set") {
                post("/card") {
                    call.safetyReceiveWithBody<CardCode> { card ->
                        logger.info("Считано значение карты ${card.card}")
                        stateController.setCard(card.card)
                        call.respond(HttpStatusCode.NoContent)
                    }
                }

                post("/qr") {
                    call.safetyReceiveWithBody<QrCode> { code ->
                        logger.info("Считано значение qr кода ${code.qr}")
                        stateController.setQr(code.qr)
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }

            get("/reset") {
                logger.info("Сброс состояния")
                stateController.reset()
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}