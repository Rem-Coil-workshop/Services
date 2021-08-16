package com.remcoil.endpoins.card

import com.remcoil.gateway.controller.slot.SlotController
import com.remcoil.utils.logger
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.cardModule() {
    val controller: SlotController by closestDI().instance()

    routing {
        webSocket("/card") {
            logger.info("Открыт сокет")
            val observer = object : SlotController.CardObserver {
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
                controller.subscribe(observer)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    if (receivedText == "BYE") {
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    }
                }
            } finally {
                logger.info("Закрываем сокет")
                controller.unsubscribe(observer)
            }
        }
    }
}