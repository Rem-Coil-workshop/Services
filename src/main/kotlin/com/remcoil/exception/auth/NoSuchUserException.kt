package com.remcoil.exception.auth

class NoSuchUserException(val firstname: String, val lastname: String) :
    AuthenticationException("Такого пользователя не существует ($firstname, $lastname)")
