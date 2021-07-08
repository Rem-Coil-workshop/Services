package com.remcoil.slots

import com.remcoil.base.BadRequestException

class SlotOpenException(override val message: String? = null) : BadRequestException(message)
