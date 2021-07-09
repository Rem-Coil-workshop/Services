package com.remcoil.slots

import com.remcoil.boxes.BoxesService
import com.remcoil.config.RoutesConfig
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.net.ConnectException

class SlotOpenerImpl(
    private val boxesService: BoxesService,
    private val client: HttpClient,
    private val routesConfig: RoutesConfig
) : SlotOpener {
    private val logger = LoggerFactory.getLogger(SlotOpener::class.java)

    override suspend fun open(qrCode: String): Boolean {
        val box = boxesService.getByQrCode(qrCode)
        return withContext(Dispatchers.IO) {
            try {
                openSlotByBoxNumber(box.number)
            } catch (e: ClientRequestException) {
                logger.info("Конечная точка для сервиса недоступна или формат неверный (${e.response.status.value})")
                false
            } catch (e: ConnectException) {
                logger.info("Не удалось уставноваить соединение с сервером")
                false
            }
        }
    }

    private suspend fun openSlotByBoxNumber(boxNumber: Int): Boolean {
        val response = client.post<HttpResponse>(urlString = routesConfig.opener) {
            contentType(ContentType.Application.Json)
            body = SlotInfo(boxNumber)
        }
        val result = response.status.isSuccess()
//        val result = true
        if (result) logger.info("Открыт ящик №$boxNumber")
        return result
    }
}