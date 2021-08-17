package com.remcoil.endpoins.user

import com.remcoil.data.model.user.Token
import com.remcoil.data.model.user.User
import com.remcoil.data.model.user.UserCredentials
import com.remcoil.gateway.service.user.UsersService
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
        val service: UsersService by closestDI().instance()

        route("/v1/users") {
            authenticate("admin") {
                get {
                    val users = service.getAll()
                    call.respond(users)
                }

                delete {
                    call.safetyReceiveWithBody<User> { user ->
                        service.remove(user)
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }

            post("/sign_in") {
                call.safetyReceiveWithBody<UserCredentials> { credential ->
                    val user = service.get(credential)
                    val token: Token by closestDI().instance(arg = user)
                    call.respond(token)
                }
            }

            post("/sign_up") {
                call.safetyReceiveWithBody<UserCredentials> { credentials ->
                    val user = service.createByCredentials(credentials)
                    val token: Token by closestDI().instance(arg = user)
                    call.respond(token)
                }
            }
        }
    }
}