package com.remcoil.presentation.module.slot

import com.remcoil.data.model.slot.CardCode
import com.remcoil.data.model.slot.QrCode
import com.remcoil.presentation.device.SlotOpener
import com.remcoil.domain.service.slot.SlotService
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotModule() {
    val service: SlotService by closestDI().instance()
    val opener: SlotOpener by closestDI().instance()

    routing {
        route("/v1/slots") {
            post("/card") {
                call.safetyReceive<CardCode> { card ->
                    service.onCardNumberEntered(card.card)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            post("/qr") {
                call.safetyReceive<QrCode> { code ->
                    service.onQrCodeEntered(code.qr)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            get("/reset") {
                service.resetState()
                call.respond(HttpStatusCode.NoContent)
            }

            get("/open/{id}") {
                call.safetyReceive("id") { id ->
                    opener.openByBoxNumber(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}