package com.remcoil.utils

inline fun <T> T.takeIfOrThrow(exception: Throwable, predicate: T.() -> Boolean): T =
    takeIf { this.predicate() } ?: throw exception
