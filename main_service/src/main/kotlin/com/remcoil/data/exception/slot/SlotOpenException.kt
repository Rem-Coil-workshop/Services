package com.remcoil.data.exception.slot

import com.remcoil.data.exception.base.BadRequestException

class SlotOpenException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
