package com.remcoil.exception.auth

class NoSuchUserException(firstname: String, lastname: String) :
    AuthenticationException("Такого пользователя не существует ($firstname, $lastname)")
