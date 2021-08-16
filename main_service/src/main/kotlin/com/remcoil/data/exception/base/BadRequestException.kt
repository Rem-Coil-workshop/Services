package com.remcoil.data.exception.base

abstract class BadRequestException(override val message: String = DEFAULT_MESSAGE) : InfoException(message) {
    companion object {
        const val DEFAULT_MESSAGE = "Ошибка в запросе пользователя"
    }
}