package com.remcoil.exception.box

import com.remcoil.exception.base.BadRequestException

class NoSuchBoxException(override val message: String? = DEFAULT_MESSAGE) : BadRequestException(message)
