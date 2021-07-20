package com.remcoil.di.slot

import com.remcoil.domain.service.slot.SlotService
import com.remcoil.domain.service.slot.SlotServiceImpl
import com.remcoil.domain.useCase.SlotValidator
import com.remcoil.domain.useCase.SlotValidatorImpl
import com.remcoil.presentation.device.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.slotsComponent() {
    bind<SlotValidator>() with singleton { SlotValidatorImpl(instance(), instance()) }

    bind<SlotService>() with singleton { SlotServiceImpl(instance(), instance(), instance(), instance()) }

    bind<SlotState>() with singleton { SlotStateImpl(instance()) }

    bind<SlotOpener>() with singleton { SlotOpenerImpl(instance(), instance(), instance()) }

    bind<Led>() with singleton { Led(instance(), instance()) }
}