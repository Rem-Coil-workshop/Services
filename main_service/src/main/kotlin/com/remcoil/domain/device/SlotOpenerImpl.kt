package com.remcoil.domain.device

import com.remcoil.config.hocon.RoutesConfig
import com.remcoil.data.model.slot.SlotInfo
import com.remcoil.domain.useCase.SlotUseCase
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
    private val client: HttpClient,
    private val routesConfig: RoutesConfig
) : SlotOpener {
    private val logger = LoggerFactory.getLogger(SlotOpener::class.java)

    override suspend fun open(slotNumber: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                safetyOpen(slotNumber)
            } catch (e: ClientRequestException) {
                logger.info("Конечная точка для сервиса недоступна или формат неверный (${e.response.status.value})")
                false
            } catch (e: ConnectException) {
                logger.info("Не удалось установить соединение с сервером")
                false
            }
        }
    }

    private suspend fun safetyOpen(slotNumber: Int): Boolean {
        val response = client.post<HttpResponse>(urlString = routesConfig.opener) {
            contentType(ContentType.Application.Json)
            body = SlotInfo(slotNumber)
        }
        val result = response.status.isSuccess()
        if (result) logger.info("Открыт ящик №$slotNumber")
        return result
    }
}