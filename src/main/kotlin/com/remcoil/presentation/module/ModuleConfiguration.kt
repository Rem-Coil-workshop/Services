package com.remcoil.presentation.module

import com.remcoil.boxes.boxesModule
import com.remcoil.cardWebsocket.cardModule
import com.remcoil.presentation.module.employee.employeeModule
import com.remcoil.presentation.module.log.logsModule
import com.remcoil.presentation.module.slot.slotModule
import com.remcoil.presentation.module.task.tasksModule
import com.remcoil.presentation.module.task.tasksModuleOld
import com.remcoil.site.siteModule
import io.ktor.application.*

fun Application.modules() {
    siteModule()

    tasksModule()
    tasksModuleOld()

    employeeModule()
    logsModule()
    boxesModule()
    slotModule()

    cardModule()
}