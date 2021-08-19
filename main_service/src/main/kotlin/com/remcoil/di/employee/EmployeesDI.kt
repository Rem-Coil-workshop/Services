package com.remcoil.di.employee

import com.remcoil.data.database.employee.EmployeesDao
import com.remcoil.data.database.employee.PermissionsDao
import com.remcoil.data.repository.EmployeeRepository
import com.remcoil.data.repository.PermissionRepository
import com.remcoil.domain.useCase.EmployeeUseCase
import com.remcoil.domain.useCase.PermissionUseCase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.employeesComponents() {
    bind<EmployeesDao>() with singleton { EmployeesDao(instance()) }
    bind<EmployeeRepository>() with singleton { EmployeeRepository(instance()) }
    bind<EmployeeUseCase>() with singleton { EmployeeUseCase(instance()) }

    bind<PermissionsDao>() with singleton { PermissionsDao(instance()) }
    bind<PermissionRepository>() with singleton { PermissionRepository(instance()) }
    bind<PermissionUseCase>() with singleton { PermissionUseCase(instance(), instance(), instance()) }
}