package com.remcoil.database

import com.remcoil.config.DatabaseConfig
import org.flywaydb.core.Flyway


fun migrate(config: DatabaseConfig) {
    Flyway
        .configure()
        .dataSource(config.url, config.user, config.password)
        .load()
        .migrate()
}