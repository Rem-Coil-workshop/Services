package com.remcoil.utils

import com.remcoil.data.exception.base.InfoException
import com.remcoil.data.exception.user.AuthenticationException
import com.remcoil.data.model.base.TextMessage
import com.remcoil.data.model.user.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

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

suspend inline fun ApplicationCall.safetyReceiveWithRouteParameter(
    routeParameter: String,
    onCorrectResult: (String) -> Unit
) {
    try {
        parameters[routeParameter]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    }
}

suspend inline fun ApplicationCall.safetyReceiveWithQueryParameter(
    queryParameterName: String,
    onCorrectResult: (String) -> Unit
) {
    try {
        request.queryParameters[queryParameterName]
            ?.let(onCorrectResult)
            ?: respond(HttpStatusCode.BadRequest)

    } catch (e: InfoException) {
        logThrowable(e)
        respond(HttpStatusCode.BadRequest, TextMessage(e.message.toString()))
    }
}

fun ApplicationCall.user() = principal<JWTPrincipal>()!!.toUser()

fun JWTPrincipal.toUser(): User {
    val firstname = payload.getClaim("firstname").asString()
    val lastname = payload.getClaim("lastname").asString()
    val role = payload.getClaim("role").asString()
    return User(firstname = firstname, lastname = lastname, role = role)
}