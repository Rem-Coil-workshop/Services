package com.remcoil.endpoins.task

import com.remcoil.data.model.base.TextMessage
import com.remcoil.data.model.task.TaskResponse
import com.remcoil.domain.useCase.TaskUseCase
import com.remcoil.utils.safetyReceiveWithBody
import com.remcoil.utils.safetyReceiveWithRouteParameter
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.tasksModuleOld() {
    val taskUseCase: TaskUseCase by closestDI().instance()

    routing {
        authenticate("admin") {
            route("/tasks") {
                get("/list") {
                    call.respond(taskUseCase.getAll())
                }

                get("/add/{taskName}") {
                    val taskName = call.parameters["taskName"]!!
                    call.respond(taskUseCase.add(taskName))
                }

                get("/delete/{taskName}") {
                    taskUseCase.delete(call.parameters["taskName"]!!)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

fun Application.tasksModule() {
    val taskUseCase: TaskUseCase by closestDI().instance()

    routing {
        authenticate("employee") {
            route("/v1/tasks") {
                get {
                    call.respond(taskUseCase.getAll())
                }

                post {
                    call.safetyReceiveWithBody<TaskResponse> { task ->
                        call.respond(taskUseCase.add(task.qrCode))
                    }
                }

                delete("/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        taskUseCase.delete(id.toInt())
                        call.respond(TextMessage("Задача удалена"))
                    }
                }
            }
        }
    }
}