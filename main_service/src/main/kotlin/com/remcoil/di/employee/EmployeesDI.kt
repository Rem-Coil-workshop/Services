package com.remcoil.di.employee

import com.remcoil.data.database.employee.EmployeesDao
import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.gateway.service.employee.EmployeesService
import com.remcoil.gateway.service.employee.PermissionsService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.employeesComponents() {
    bind<EmployeesDao>() with singleton { EmployeesDao(instance()) }
    bind<EmployeesService>() with singleton { EmployeesService(instance()) }

    bind<PermissionsDao>() with singleton { PermissionsDao(instance()) }
    bind<PermissionsService>() with singleton { PermissionsService(instance(), instance(), instance()) }
}