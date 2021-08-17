package com.remcoil.endpoins.task

import com.remcoil.data.model.base.TextMessage
import com.remcoil.data.model.task.TaskResponse
import com.remcoil.gateway.service.task.TasksService
import com.remcoil.utils.logger
import com.remcoil.utils.safetyReceiveWithBody
import com.remcoil.utils.safetyReceiveWithRouteParameter
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.tasksModuleOld() {
    val service: TasksService by closestDI().instance()

    routing {
        authenticate("admin") {
            route("/tasks") {
                get("/list") {
                    call.respond(service.getAll())
                }

                get("/add/{taskName}") {
                    val taskName = call.parameters["taskName"]!!
                    call.respond(service.addTask(taskName))
                }

                get("/delete/{taskName}") {
                    service.deleteTask(call.parameters["taskName"]!!)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

fun Application.tasksModule() {
    val service: TasksService by closestDI().instance()

    routing {
        authenticate("employee") {
            route("/v1/tasks") {
                get {
                    call.respond(service.getAll())
                }

                post {
                    call.safetyReceiveWithBody<TaskResponse> { task ->
                        call.respond(service.addTask(task.qrCode))
                    }
                }

                delete("/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        service.deleteTask(id.toInt())
                        call.respond(TextMessage("Задача удалена"))
                    }
                }
            }
        }
    }
}