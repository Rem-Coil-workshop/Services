package com.remcoil.di.slot

import com.remcoil.data.database.slot.SlotsDao
import com.remcoil.domain.validator.EmployeeDataValidator
import com.remcoil.domain.validator.EmployeeDataValidatorImpl
import com.remcoil.domain.device.*
import com.remcoil.gateway.controller.slot.SlotStateController
import com.remcoil.gateway.service.slot.SlotsService
import com.remcoil.gateway.service.slot.SlotStateService
import com.remcoil.gateway.service.slot.SlotStateServiceImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.slotsComponent() {
    bind<SlotsDao>() with singleton { SlotsDao((instance())) }
    bind<SlotsService>() with singleton { SlotsService(instance(), instance()) }

    bind<SlotStateController>() with singleton { SlotStateController(instance()) }

    bind<SlotStateService>() with singleton { SlotStateServiceImpl(instance(), instance(), instance(), instance()) }
    bind<EmployeeDataValidator>() with singleton { EmployeeDataValidatorImpl(instance(), instance(), instance()) }

    bind<SlotState>() with singleton { SlotStateImpl(instance()) }
    bind<SlotOpener>() with singleton { SlotOpenerImpl(instance(), instance(), instance()) }
    bind<Led>() with singleton { Led(instance(), instance()) }
}