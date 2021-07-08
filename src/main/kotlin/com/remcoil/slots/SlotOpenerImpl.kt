package com.remcoil.slots

import com.remcoil.boxes.BoxesService
import com.remcoil.config.RoutesConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class SlotOpenerImpl(
    private val boxesService: BoxesService,
    private val client: HttpClient,
    private val routesConfig: RoutesConfig
) : SlotOpener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun open(qrCode: String): Boolean {
        val box = boxesService.getByQrCode(qrCode)
        return runBlocking(Dispatchers.IO) {
            openSlotByBoxNumber(box.number)
        }
    }

    private suspend fun openSlotByBoxNumber(boxNumber: Int): Boolean {
        logger.info("Open $boxNumber box")
        return true
//        return client.post<HttpResponse>(urlString = routesConfig.opener) {
//            contentType(ContentType.Application.Json)
//            body = SlotInfo(boxNumber)
//        }.status.isSuccess()
    }
}