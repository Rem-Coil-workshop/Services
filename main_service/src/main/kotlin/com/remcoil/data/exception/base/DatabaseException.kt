package com.remcoil.data.exception.base

class DatabaseException(override val message: String? = "Ошибка при выполнении запроса в базу данных") :
    InfoException(message)