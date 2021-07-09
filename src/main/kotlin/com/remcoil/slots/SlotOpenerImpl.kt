package com.remcoil.slots

import com.remcoil.boxes.BoxesService
import com.remcoil.config.RoutesConfig
import com.remcoil.utils.logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SlotOpenerImpl(
    private val boxesService: BoxesService,
    private val client: HttpClient,
    private val routesConfig: RoutesConfig
) : SlotOpener {
    override suspend fun open(qrCode: String): Boolean {
        val box = boxesService.getByQrCode(qrCode)
        return withContext(Dispatchers.IO) {
            openSlotByBoxNumber(box.number)
        }
    }

    private suspend fun openSlotByBoxNumber(boxNumber: Int): Boolean {
        val result = client.post<HttpResponse>(urlString = routesConfig.opener) {
            contentType(ContentType.Application.Json)
            body = SlotInfo(boxNumber)
        }.status.isSuccess()
//        val result = true
        if (result) logger.info("Открыт ящик №$boxNumber")
        return result
    }
}