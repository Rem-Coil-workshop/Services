package com.remcoil.data.exception.slot

import com.remcoil.data.exception.base.BadRequestException

class TaskNotUniqueException(override val message: String = "Введено не уникальное значение задачи") :
    BadRequestException(message)
