package com.remcoil.exception.auth

abstract class AuthenticationException(override val message: String = "Неверные данные пользователя") : Throwable()
