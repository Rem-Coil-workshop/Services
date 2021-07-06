package com.remcoil.tasks

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
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
                val deleteResult = service.deleteTask(call.parameters["taskName"]!!)
                call.application.environment.log.info(deleteResult.toString())
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}

fun Application.tasksModule() {
    val service: TasksService by closestDI().instance()

    routing {
        route("/v1/tasks") {
            get("/list") {
                call.respond(service.getTasks())
            }

            post {
                call.safetyReceive<TaskResponse> { task ->
                    call.respond(service.addTask(task.name))
                }
            }

            delete {
                call.safetyReceive<TaskResponse> { task ->
                    service.deleteTask(task.name)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

@Serializable
data class TaskResponse(val name: String)