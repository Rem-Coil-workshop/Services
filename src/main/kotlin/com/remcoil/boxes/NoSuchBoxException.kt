package com.remcoil.boxes

import com.remcoil.base.BadRequestException

class NoSuchBoxException(override val message: String? = null) : BadRequestException(message)
