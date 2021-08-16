package com.remcoil.data.exception.base

abstract class InfoException(override val message: String? = null) : RuntimeException(message)