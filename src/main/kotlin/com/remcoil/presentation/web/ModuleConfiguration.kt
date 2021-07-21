package com.remcoil.presentation.web

import com.remcoil.presentation.web.box.boxesModule
import com.remcoil.presentation.web.card.cardModule
import com.remcoil.presentation.web.employee.employeeModule
import com.remcoil.presentation.web.log.logsModule
import com.remcoil.presentation.web.slot.slotModule
import com.remcoil.presentation.web.task.tasksModule
import com.remcoil.presentation.web.task.tasksModuleOld
import com.remcoil.presentation.web.site.siteModule
import com.remcoil.presentation.web.user.userModule
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

    userModule()
}