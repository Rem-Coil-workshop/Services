package com.remcoil.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

suspend inline fun <reified T : Any> ApplicationCall.safetyReceive(onCorrectResult: (T) -> Unit) {
    try {
        receiveOrNull<T>()
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: Exception) { // TODO: 06.07.2021 Change exception type
        application.environment.log.error(e.message)
        respond(HttpStatusCode.BadRequest)
    }
}

suspend inline fun ApplicationCall.safetyReceive(parameterName: String, onCorrectResult: (String) -> Unit) {
    try {
        parameters[parameterName]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: Exception) { // TODO: 06.07.2021 Change exception type
        application.environment.log.error(e.message)
        respond(HttpStatusCode.BadRequest)
    }
}