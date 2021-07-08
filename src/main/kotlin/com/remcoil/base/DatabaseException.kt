package com.remcoil.base

class DatabaseException(override val message: String? = "Ошибка при выполнении запроса в базу данных") :
    InfoException(message)