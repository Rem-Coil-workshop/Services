package com.remcoil.presentation.module.task

import com.remcoil.data.model.base.TextMessage
import com.remcoil.data.model.task.TaskResponse
import com.remcoil.domain.service.task.TasksService
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.tasksModuleOld() {
    val service: TasksService by closestDI().instance()

    routing {
        route("/tasks") {
            get("/list") {
                call.respond(service.getTasks())
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

fun Application.tasksModule() {
    val service: TasksService by closestDI().instance()

    routing {
        route("/v1/tasks") {
            get {
                call.respond(service.getTasks())
            }

            post {
                call.safetyReceive<TaskResponse> { task ->
                    call.respond(service.addTask(task.qrCode))
                }
            }

            delete("/{id}") {
                call.safetyReceive("id") { id ->
                    service.deleteTask(id.toInt())
                    call.respond(TextMessage("Задача удалена"))
                }
            }
        }
    }
}