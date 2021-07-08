package com.remcoil.employees

import com.remcoil.base.BadRequestException

class NoSuchEmployeeException(override val message: String = "") : BadRequestException(message)