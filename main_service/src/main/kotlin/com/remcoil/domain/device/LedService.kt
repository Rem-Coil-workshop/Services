package com.remcoil.domain.device

import com.remcoil.config.hocon.RoutesConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LedService(
    private val client: HttpClient,
    private val config: RoutesConfig
) {
    suspend fun turnCard() {
        sendResponse("/turn_card")
    }

    suspend fun turnQr() {
        sendResponse("/turn_qr")
    }

    suspend fun turnOff() {
        sendResponse("/turn_off")
    }

    private suspend fun sendResponse(endPoint: String) {
        val url = config.led + endPoint
        coroutineScope {
            launch(Dispatchers.IO) {
                client.get<HttpResponse>(urlString = url).status.isSuccess()
            }
        }
    }
}