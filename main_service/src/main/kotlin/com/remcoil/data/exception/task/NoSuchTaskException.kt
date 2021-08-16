package com.remcoil.data.exception.task

import com.remcoil.data.exception.base.BadRequestException

class NoSuchTaskException(override val message: String = DEFAULT_MESSAGE) : BadRequestException(message)
