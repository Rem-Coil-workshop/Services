package com.remcoil.endpoins.user

import com.remcoil.data.model.user.Token
import com.remcoil.data.model.user.User
import com.remcoil.data.model.user.UserCredentials
import com.remcoil.domain.useCase.UserUseCase
import com.remcoil.utils.safetyReceiveWithBody
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.userModule() {
    routing {
        val userUseCase: UserUseCase by closestDI().instance()

        route("/v1/users") {
            authenticate("admin") {
                delete {
                    call.safetyReceiveWithBody<User> { user ->
                        userUseCase.remove(user)
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }

            get {
                val users = userUseCase.getAll()
                call.respond(users)
            }

            post("/sign_in") {
                call.safetyReceiveWithBody<UserCredentials> { credential ->
                    val user = userUseCase.get(credential)
                    val token: Token by closestDI().instance(arg = user)
                    call.respond(token)
                }
            }

            post("/sign_up") {
                call.safetyReceiveWithBody<UserCredentials> { credentials ->
                    val user = userUseCase.createByCredentials(credentials)
                    val token: Token by closestDI().instance(arg = user)
                    call.respond(token)
                }
            }
        }
    }
}