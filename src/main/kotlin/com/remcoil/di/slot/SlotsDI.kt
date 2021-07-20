package com.remcoil.slots

import com.remcoil.useCase.service.slot.SlotService
import com.remcoil.useCase.service.slot.SlotServiceImpl
import com.remcoil.presentation.device.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.slotsComponent() {
    bind<SlotService>() with singleton { SlotServiceImpl(instance(), instance(), instance(), instance(), instance()) }

    bind<SlotState>() with singleton { SlotStateImpl(instance()) }

    bind<SlotOpener>() with singleton { SlotOpenerImpl(instance(), instance(), instance()) }

    bind<Led>() with singleton { Led(instance(), instance()) }
}