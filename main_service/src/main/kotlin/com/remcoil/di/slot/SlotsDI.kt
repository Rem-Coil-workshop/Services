package com.remcoil.di.slot

import com.remcoil.data.database.slot.SlotsDao
import com.remcoil.data.repository.SlotRepository
import com.remcoil.domain.device.LedService
import com.remcoil.domain.device.SlotOpener
import com.remcoil.domain.device.SlotOpenerImpl
import com.remcoil.domain.device.SlotStateEntity
import com.remcoil.domain.useCase.SlotStateUseCase
import com.remcoil.domain.useCase.SlotUseCase
import com.remcoil.domain.validator.EmployeeDataValidator
import com.remcoil.gateway.controller.slot.SlotStateController
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.slotsComponent() {
    bind<SlotsDao>() with singleton { SlotsDao((instance())) }
    bind<SlotRepository>() with singleton { SlotRepository(instance()) }
    bind<SlotUseCase>() with singleton { SlotUseCase(instance(), instance()) }

    bind<SlotStateController>() with singleton { SlotStateController(instance()) }

    bind<SlotStateUseCase>() with singleton { SlotStateUseCase(instance(), instance(), instance()) }
    bind<EmployeeDataValidator>() with singleton { EmployeeDataValidator(instance(), instance(), instance()) }
    bind<SlotStateEntity>() with singleton { SlotStateEntity(instance(), instance(), instance()) }

    bind<SlotOpener>() with singleton { SlotOpenerImpl(instance(), instance()) }
    bind<LedService>() with singleton { LedService(instance(), instance()) }
}