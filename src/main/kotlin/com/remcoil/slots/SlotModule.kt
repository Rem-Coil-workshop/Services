package com.remcoil.slots

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
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
                    service.setCardNumber(card.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            get("/qr/{code}") {
                call.safetyReceive("code") { code ->
                    if (service.setQrCode(code)) call.respond(HttpStatusCode.NoContent)
                    else call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/reset") {
                service.resetState()
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}