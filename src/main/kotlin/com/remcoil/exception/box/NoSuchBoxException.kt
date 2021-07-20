package com.remcoil.exception.box

import com.remcoil.exception.base.BadRequestException

class NoSuchBoxException(override val message: String? = null) : BadRequestException(message)
