package com.remcoil.boxes

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.boxesComponents() {
    bind<BoxesDao>() with singleton { BoxesDao((instance())) }
    bind<BoxesService>() with singleton { BoxesService(instance(), instance()) }
}