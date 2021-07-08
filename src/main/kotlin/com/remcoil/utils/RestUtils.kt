package com.remcoil.utils

import com.remcoil.base.InfoException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

suspend inline fun <reified T : Any> ApplicationCall.safetyReceive(onCorrectResult: (T) -> Unit) {
    try {
        receiveOrNull<T>()
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest)
    }
}

suspend inline fun ApplicationCall.safetyReceive(parameterName: String, onCorrectResult: (String) -> Unit) {
    try {
        parameters[parameterName]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest)
    }
}

fun ApplicationCall.logThrowable(e: Throwable) {
    application.environment.log.error("${e.message} (${e.javaClass.name})")
}