package com.remcoil.endpoins.employee

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.domain.useCase.EmployeeUseCase
import com.remcoil.domain.useCase.PermissionUseCase
import com.remcoil.utils.safetyReceiveWithBody
import com.remcoil.utils.safetyReceiveWithQueryParameter
import com.remcoil.utils.safetyReceiveWithRouteParameter
import com.remcoil.utils.user
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.employeeModule() {
    val employeeUseCase: EmployeeUseCase by closestDI().instance()

    routing {
        authenticate("employee") {
            route("/v1/employees") {
                get {
                    call.respond(employeeUseCase.getAll())
                }

                get("/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        call.respond(employeeUseCase.getById(id.toInt()))
                    }
                }

                post {
                    call.safetyReceiveWithBody<Employee> { employee ->
                        call.respond(employeeUseCase.add(employee))
                    }
                }

                delete("/{id}") {
                    call.safetyReceiveWithRouteParameter("id") { id ->
                        employeeUseCase.delete(id.toInt())
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }
        }
    }
}

fun Application.permissionModule() {
    val permissionUseCase: PermissionUseCase by closestDI().instance()

    routing {
        authenticate("employee") {
            route("/v1/permissions") {
                get("/employees") {
                    call.safetyReceiveWithQueryParameter("task") { taskId ->
                        val tasks = permissionUseCase.getPermittedEmployees(taskId.toInt())
                        call.respond(tasks)
                    }
                }

                get("/tasks") {
                    call.safetyReceiveWithQueryParameter("employee") { employeeId ->
                        val employees = permissionUseCase.getPermittedTasks(employeeId.toInt())
                        call.respond(employees)
                    }
                }

                post {
                    call.safetyReceiveWithBody<Permission> { permission ->
                        permissionUseCase.add(permission, call.user())
                        call.respond(HttpStatusCode.OK)
                    }
                }

                delete {
                    call.safetyReceiveWithBody<Permission> { permission ->
                        permissionUseCase.delete(permission, call.user())
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }
        }
    }
}