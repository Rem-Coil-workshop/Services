package com.remcoil.endpoins

import com.remcoil.endpoins.card.cardModule
import com.remcoil.endpoins.employee.employeeModule
import com.remcoil.endpoins.log.logsModule
import com.remcoil.endpoins.slot.slotModule
import com.remcoil.endpoins.task.tasksModule
import com.remcoil.endpoins.task.tasksModuleOld
import com.remcoil.endpoins.site.siteModule
import com.remcoil.endpoins.user.userModule
import io.ktor.application.*

fun Application.modules() {
    siteModule()

    tasksModule()
    tasksModuleOld()

    employeeModule()
    logsModule()
    slotModule()

    cardModule()

    userModule()
}