package com.remcoil.data.exception.user

class NoSuchUserException(firstname: String, lastname: String) :
    AuthenticationException("Такого пользователя не существует ($firstname, $lastname)")
