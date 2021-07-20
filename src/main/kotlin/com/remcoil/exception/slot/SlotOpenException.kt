package com.remcoil.exception.slot

import com.remcoil.exception.base.BadRequestException

class SlotOpenException(override val message: String? = null) : BadRequestException(message)
