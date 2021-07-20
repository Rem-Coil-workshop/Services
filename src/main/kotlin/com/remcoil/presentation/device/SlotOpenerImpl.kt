package com.remcoil.presentation.device

import com.remcoil.useCase.service.box.BoxesService
import com.remcoil.config.RoutesConfig
import com.remcoil.data.model.slot.SlotInfo
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

    override suspend fun openByQrCode(qrCode: String): Boolean {
        val box = boxesService.getByQrCode(qrCode)
        return safetyOpen(box.number)
    }

    override suspend fun openByBoxNumber(id: Int): Boolean {
        val box = boxesService.getById(id)
        return safetyOpen(box.number)
    }

    private suspend fun safetyOpen(boxNumber: Int): Boolean {
        return withContext(Dispatchers.IO)
        {
            try {
                open(boxNumber)
            } catch (e: ClientRequestException) {
                logger.info("Конечная точка для сервиса недоступна или формат неверный (${e.response.status.value})")
                false
            } catch (e: ConnectException) {
                logger.info("Не удалось уставноваить соединение с сервером")
                false
            }
        }
    }

    private suspend fun open(boxNumber: Int): Boolean {
        val response = client.post<HttpResponse>(urlString = routesConfig.opener) {
            contentType(ContentType.Application.Json)
            body = SlotInfo(boxNumber)
        }
        val result = response.status.isSuccess()
        if (result) logger.info("Открыт ящик №$boxNumber")
        return result
    }
}