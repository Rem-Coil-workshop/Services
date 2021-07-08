package com.remcoil.base

abstract class BadRequestException(override val message: String? = "Ошибка в запросе пользователя") :
    InfoException(message)