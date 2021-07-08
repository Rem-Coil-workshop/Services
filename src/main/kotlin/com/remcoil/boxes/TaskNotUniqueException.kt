package com.remcoil.boxes

import com.remcoil.base.BadRequestException

class TaskNotUniqueException(override val message: String? = "Введено не уникальное значение задачи") :
    BadRequestException(message)
