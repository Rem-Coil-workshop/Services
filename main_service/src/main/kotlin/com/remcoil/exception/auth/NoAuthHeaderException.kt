package com.remcoil.exception.auth

class NoAuthHeaderException : AuthenticationException("Не передан токен")