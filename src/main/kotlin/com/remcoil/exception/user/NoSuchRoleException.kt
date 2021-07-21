package com.remcoil.exception.user

import com.remcoil.exception.base.BadRequestException

class NoSuchRoleException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
