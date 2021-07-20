package com.remcoil.exception.task

import com.remcoil.exception.base.BadRequestException

class NoSuchTaskException(override val message: String? = null) : BadRequestException(message)
