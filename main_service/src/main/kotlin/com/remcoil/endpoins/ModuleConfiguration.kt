package com.remcoil.endpoins

import com.remcoil.endpoins.employee.employeeModule
import com.remcoil.endpoins.employee.permissionModule
import com.remcoil.endpoins.log.logsModule
import com.remcoil.endpoins.slot.slotModule
import com.remcoil.endpoins.task.tasksModule
import com.remcoil.endpoins.task.tasksModuleOld
import com.remcoil.endpoins.app.appModule
import com.remcoil.endpoins.slot.slotStateModule
import com.remcoil.endpoins.user.userModule
import io.ktor.application.*

fun Application.modules() {
    appModule()

    slotModule()
    slotStateModule()

    tasksModule()
    tasksModuleOld()

    employeeModule()
    permissionModule()

    logsModule()

    userModule()
}