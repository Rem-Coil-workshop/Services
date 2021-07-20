package com.remcoil.exception.box

import com.remcoil.exception.base.BadRequestException

class TaskNotUniqueException(override val message: String? = "Введено не уникальное значение задачи") :
    BadRequestException(message)
