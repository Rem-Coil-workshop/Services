package com.remcoil.data.exception.employee

import com.remcoil.data.exception.base.BadRequestException

class NoSuchEmployeeException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)