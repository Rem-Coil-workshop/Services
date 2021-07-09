package com.remcoil.utils

import com.remcoil.base.InfoException
import com.remcoil.base.TextMessage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

suspend inline fun <reified T : Any> ApplicationCall.safetyReceive(onCorrectResult: (T) -> Unit) {
    try {
        receiveOrNull<T>()
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    }
}

suspend inline fun ApplicationCall.safetyReceive(parameterName: String, onCorrectResult: (String) -> Unit) {
    try {
        parameters[parameterName]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    }
}

fun ApplicationCall.logThrowable(e: Throwable) {
    application.environment.log.info("${e.message} (${e.javaClass.name})")
}

val Any.logger: Logger get() = LoggerFactory.getLogger(this::class.java)