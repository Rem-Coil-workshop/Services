package com.remcoil.data.exception.user

class NoAuthHeaderException : AuthenticationException("Не передан токен")