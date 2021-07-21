package com.remcoil.exception.task

import com.remcoil.exception.base.BadRequestException

class NoSuchTaskException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
