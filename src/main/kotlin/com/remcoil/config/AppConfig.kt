package com.remcoil.config

data class AppConfig(
    val http: HttpConfig,
    val database: DatabaseConfig,
    val routes: RoutesConfig,
    val secure: SecureConfig
)
