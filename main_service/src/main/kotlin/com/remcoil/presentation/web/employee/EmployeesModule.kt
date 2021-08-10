package com.remcoil.presentation.web.employee

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.domain.service.employee.EmployeesService
import com.remcoil.domain.service.employee.PermissionsService
import com.remcoil.utils.safetyReceiveWithBody
import com.remcoil.utils.safetyReceiveWithQueryParameter
import com.remcoil.utils.safetyReceiveWithRouteParameter
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.employeeModule() {
    val employeeService: EmployeesService by closestDI().instance()
    val permissionsService: PermissionsService by closestDI().instance()

    routing {
        route("/v1/employees") {
            get {
                call.respond(employeeService.getAll())
            }

            get("/{id}") {
                call.safetyReceiveWithRouteParameter("id") { id ->
                    call.respond(employeeService.getById(id.toInt()))
                }
            }

            post {
                call.safetyReceiveWithBody<Employee> { employee ->
                    call.respond(employeeService.addEmployee(employee))
                }
            }

            delete("/{id}") {
                call.safetyReceiveWithRouteParameter("id") { id ->
                    employeeService.removeById(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }

        route("/v1/permissions") {
            get("/employees") {
                call.safetyReceiveWithQueryParameter("task") { taskId ->
                    val tasks = permissionsService.getPermittedEmployees(taskId.toInt())
                    call.respond(tasks)
                }
            }

            get("/tasks") {
                call.safetyReceiveWithQueryParameter("employee") { employeeId ->
                    val employees = permissionsService.getPermittedTasks(employeeId.toInt())
                    call.respond(employees)
                }
            }

            post {
                call.safetyReceiveWithBody<Permission> { permission ->
                    permissionsService.addPermission(permission)
                    call.respond(HttpStatusCode.OK)
                }
            }

            delete {
                call.safetyReceiveWithBody<Permission> { permission ->
                    permissionsService.deletePermission(permission)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}