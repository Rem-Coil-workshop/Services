package com.remcoil.slots

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.slotsComponent() {
    bind<SlotService>() with singleton { SlotServiceImpl(instance(), instance(), instance(), instance(), instance()) }

    bind<SlotState>() with singleton { SingleSlotState(instance()) }

    bind<SlotOpener>() with singleton { SlotOpenerImpl(instance(), instance(), instance()) }

    bind<LedHelper>() with singleton { LedHelper(instance(), instance()) }
}