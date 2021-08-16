package com.remcoil.data.exception.slot

import com.remcoil.data.exception.base.BadRequestException

class NoSuchSlotException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
