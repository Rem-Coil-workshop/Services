package com.remcoil.slots

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotModule() {
    val service: SlotService by closestDI().instance()

    routing {
        route("/v1/slots") {
            get("/card/{card}") {
                call.safetyReceive("card") { card ->
                    if (service.setCardNumber(card.toInt())) call.respond(HttpStatusCode.NoContent)
                    else call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/qr/{code}") {
                call.safetyReceive("code") { code ->
                    if (service.setQrCode(code)) call.respond(HttpStatusCode.NoContent)
                    else call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("button") {
                service.resetState()
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}