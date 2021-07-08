package com.remcoil.employees

import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.employeeModule() {
    val service: EmployeesService by closestDI().instance()

    routing {
        route("/v1/employees") {
            get {
                call.respond(service.getAll())
            }

            get("/id/{id}") {
                call.safetyReceive("id") { id ->
                    call.respond(service.getById(id.toInt()))
                }
            }

            get("/number/{number}") {
                call.safetyReceive("number") { number ->
                    call.respond(service.getByEmployeeNumber(number.toInt()))
                }
            }

            post {
                call.safetyReceive<Employee> { employee ->
                    call.respond(service.addEmployee(employee))
                }
            }

            delete("/id/{id}") {
                call.safetyReceive("id") { id ->
                    service.removeById(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }

            delete("/number/{number}") {
                call.safetyReceive("number") { number ->
                    service.removeByEmployeeNumber(number.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}