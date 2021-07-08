package com.remcoil.logs

import com.remcoil.base.BadRequestException

class OutOfLogsException(override val message: String? = null) : BadRequestException(message)
