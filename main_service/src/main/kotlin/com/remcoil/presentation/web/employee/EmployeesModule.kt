package com.remcoil.presentation.web.employee

import com.remcoil.data.model.employee.Employee
import com.remcoil.data.model.employee.Permission
import com.remcoil.domain.service.employee.EmployeesService
import com.remcoil.domain.service.employee.PermissionsService
import com.remcoil.utils.safetyReceive
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
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
                call.safetyReceive("id") { id ->
                    call.respond(employeeService.getById(id.toInt()))
                }
            }

            post {
                call.safetyReceive<Employee> { employee ->
                    call.respond(employeeService.addEmployee(employee))
                }
            }

            delete("/{id}") {
                call.safetyReceive("id") { id ->
                    employeeService.removeById(id.toInt())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }

        route("/v1/permissions") {
            get {
                call.safetyReceive<Employee> { employee ->
                    val tasks = permissionsService.getPermissions(employee)
                    call.respond(TaskIdList(tasks))
                }
            }

            post {
                call.safetyReceive<Permission> { permission ->
                    permissionsService.addPermission(permission)
                    call.respond(HttpStatusCode.OK)
                }
            }

            delete {
                call.safetyReceive<Permission> { permission ->
                    permissionsService.deletePermission(permission)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

@Serializable
data class TaskIdList(val tasksId: List<Int>)