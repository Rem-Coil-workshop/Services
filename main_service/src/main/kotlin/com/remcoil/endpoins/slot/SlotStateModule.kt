package com.remcoil.endpoins.slot

import com.remcoil.data.model.slot.CardCode
import com.remcoil.data.model.slot.QrCode
import com.remcoil.controller.slot.SlotStateController
import com.remcoil.utils.logger
import com.remcoil.utils.safetyReceiveWithBody
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.slotStateModule() {
    val stateController: SlotStateController by closestDI().instance()

    routing {
        route("/v1/slots") {
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
                stateController.reset()
                call.respond(HttpStatusCode.NoContent)
            }
        }

        webSocket("/card") {
            logger.info("Открыт сокет")
            val observer = object : SlotStateController.CardObserver {
                override suspend fun onCardEntered(card: Int) {
                    logger.info("Отправлено значение карты $card")
                    outgoing.send(Frame.Text(card.toString()))
                }

                override suspend fun onClose() {
                    close(
                        CloseReason(
                            CloseReason.Codes.NORMAL,
                            "Соединений с сервером закрыто, попробуйте перезайти в диалог"
                        )
                    )
                }
            }

            try {
                stateController.subscribe(observer)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    if (receivedText == "BYE") {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            } finally {
                logger.info("Закрываем сокет")
                stateController.unsubscribe(observer)
            }
        }
    }
}