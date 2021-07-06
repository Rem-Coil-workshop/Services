package com.remcoil.employees

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.employeesComponents() {
    bind<EmployeesDao>() with singleton { EmployeesDao(instance()) }
    bind<EmployeesService>() with singleton { EmployeesService(instance()) }
}