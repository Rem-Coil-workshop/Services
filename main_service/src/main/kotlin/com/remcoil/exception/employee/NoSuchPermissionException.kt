package com.remcoil.exception.employee

import com.remcoil.exception.base.BadRequestException

class NoSuchPermissionException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)