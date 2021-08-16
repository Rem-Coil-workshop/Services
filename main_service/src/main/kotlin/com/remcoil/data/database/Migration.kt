package com.remcoil.data.database

import com.remcoil.config.hocon.DatabaseConfig
import org.flywaydb.core.Flyway


fun migrate(config: DatabaseConfig) {
    Flyway
        .configure()
        .dataSource(config.url, config.user, config.password)
        .load()
        .migrate()
}