package com.remcoil.slots

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotModule() {
    val service: SlotService by closestDI().instance()

    routing {
        route("/v1/slots") {
            post("/card") {
                call.safetyReceive<CardCode> { card ->
                    service.setCardNumber(card.card)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            post("/qr") {
                call.safetyReceive<QrCode> { code ->
                    if (service.setQrCode(code.qr)) call.respond(HttpStatusCode.NoContent)
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

@Serializable
data class QrCode(val qr: String)

@Serializable
data class CardCode(val card: Int)