package com.remcoil.utils

import com.remcoil.data.exception.base.InfoException
import com.remcoil.data.model.base.TextMessage
import com.remcoil.data.model.user.Token
import com.remcoil.data.exception.user.AuthenticationException
import com.remcoil.data.exception.user.NoAuthHeaderException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

suspend inline fun <reified T : Any> ApplicationCall.safetyReceiveWithBody(onCorrectResult: (T) -> Unit) {
    try {
        receiveOrNull<T>()
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: AuthenticationException) {
        logThrowable(e)
        respond(HttpStatusCode.Unauthorized, TextMessage(e.message))
    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    } catch (e: Throwable) {
        logThrowable(e)
        respond(HttpStatusCode.InternalServerError)
    }
}

suspend inline fun ApplicationCall.safetyReceiveWithRouteParameter(routeParameter: String, onCorrectResult: (String) -> Unit) {
    try {
        parameters[routeParameter]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    }
}

suspend inline fun ApplicationCall.safetyReceiveWithQueryParameter(queryParameterName: String, onCorrectResult: (String) -> Unit) {
    try {
        request.queryParameters[queryParameterName]
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

val ApplicationCall.logger: Logger get() = application.environment.log

val Any.logger: Logger get() = LoggerFactory.getLogger(this::class.java)

fun String?.toToken(): Token =
    this?.let { header -> Token(header.removePrefix("Bearer ")) }
        ?: throw NoAuthHeaderException()
