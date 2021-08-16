package com.remcoil.data.exception.employee

import com.remcoil.data.exception.base.BadRequestException

class NoSuchPermissionException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)