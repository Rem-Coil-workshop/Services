package com.remcoil.tasks

import com.remcoil.base.BadRequestException

class NoSuchTaskException(override val message: String? = null) : BadRequestException(message)
