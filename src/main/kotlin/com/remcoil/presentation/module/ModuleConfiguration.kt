package com.remcoil.presentation.module

import com.remcoil.presentation.module.box.boxesModule
import com.remcoil.presentation.module.card.cardModule
import com.remcoil.presentation.module.employee.employeeModule
import com.remcoil.presentation.module.log.logsModule
import com.remcoil.presentation.module.slot.slotModule
import com.remcoil.presentation.module.task.tasksModule
import com.remcoil.presentation.module.task.tasksModuleOld
import com.remcoil.presentation.module.site.siteModule
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