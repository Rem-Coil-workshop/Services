package com.remcoil.exception.log

import com.remcoil.exception.base.BadRequestException

class OutOfLogsException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
