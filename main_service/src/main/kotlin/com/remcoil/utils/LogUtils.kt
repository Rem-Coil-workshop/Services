package com.remcoil.utils

import io.ktor.application.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun ApplicationCall.logThrowable(e: Throwable) {
    application.environment.log.info("${e.message} (${e.javaClass.name})")
}

val Any.logger: Logger get() = LoggerFactory.getLogger(this::class.java)

inline fun <T> Any.logged(message: String, block: () -> T): T {
    val result = block()
    logger.info(message)
    return result
}

inline fun <T> Any.loggedEntity(messageGenerator: (T) -> String, block: () -> T): T {
    val result = block()
    val message = messageGenerator(result)
    logger.info(message)
    return result
}