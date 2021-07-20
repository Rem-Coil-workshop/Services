package com.remcoil.presentation.module.card

import com.remcoil.domain.controller.slot.CardObserver
import com.remcoil.domain.controller.slot.SlotController
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.cardModule() {
    val controller: SlotController by closestDI().instance()

    routing {
        webSocket("/websocket") {
            val observer = object : CardObserver {
                override suspend fun onCardEntered(card: Int) {
                    outgoing.send(Frame.Text(card.toString()))
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
                controller.unsubscribe(observer)
            }
        }
    }
}