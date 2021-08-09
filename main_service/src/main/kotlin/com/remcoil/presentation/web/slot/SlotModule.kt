package com.remcoil.presentation.web.slot

import com.remcoil.data.model.slot.CardCode
import com.remcoil.data.model.slot.QrCode
import com.remcoil.domain.controller.slot.SlotController
import com.remcoil.presentation.device.SlotOpener
import com.remcoil.utils.logger
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotModule() {
    val controller: SlotController by closestDI().instance()
    val opener: SlotOpener by closestDI().instance()

    routing {
        route("/v1/slots") {
            post("/card") {
                call.safetyReceive<CardCode> { card ->
                    logger.info("Считано значение карты ${card.card}")
                    controller.setCard(card.card)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            post("/qr") {
                call.safetyReceive<QrCode> { code ->
                    logger.info("Считано значение qr кода ${code.qr}")
                    controller.setQr(code.qr)
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            get("/reset") {
                logger.info("Сброс состояния")
                controller.reset()
                call.respond(HttpStatusCode.NoContent)
            }

            get("/open/{id}") {
                call.safetyReceive("id") { id ->
                    logger.info("Открываем ячейку $id")
                    opener.openByBoxNumber(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}