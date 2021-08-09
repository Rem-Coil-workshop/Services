package com.remcoil.exception.base

abstract class InfoException(override val message: String? = null) : RuntimeException(message)