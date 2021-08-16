package com.remcoil.data.exception.user

import com.remcoil.data.exception.base.BadRequestException

class NoSuchRoleException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
