package com.remcoil.config.hocon

data class AppConfig(
    val http: HttpConfig,
    val database: DatabaseConfig,
    val routes: RoutesConfig,
    val secure: SecureConfig,
    val logSource: LogFileConfig,
)
