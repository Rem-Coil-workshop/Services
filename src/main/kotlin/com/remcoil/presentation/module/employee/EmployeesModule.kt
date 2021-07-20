package com.remcoil.presentation.module.employee

import com.remcoil.data.model.employee.Employee
import com.remcoil.domain.service.employee.EmployeesService
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

            get("/{id}") {
                call.safetyReceive("id") { id ->
                    call.respond(service.getById(id.toInt()))
                }
            }

            post {
                call.safetyReceive<Employee> { employee ->
                    call.respond(service.addEmployee(employee))
                }
            }

            delete("/{id}") {
                call.safetyReceive("id") { id ->
                    service.removeById(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}