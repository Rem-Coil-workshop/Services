package com.remcoil.employees

class NoSuchEmployeeException(override val message: String = "") : Exception(message)