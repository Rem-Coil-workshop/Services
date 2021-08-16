package com.remcoil.data.exception.user

abstract class AuthenticationException(override val message: String = "Неверные данные пользователя") : Throwable()
