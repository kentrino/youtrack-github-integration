package com.kentrino.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource


fun createHikariDataSource(): HikariDataSource {
    val config = HikariConfig().apply {
        driverClassName = "com.mysql.cj.jdbc.Driver"
        jdbcUrl = "jdbc:mysql://localhost/test"
        addDataSourceProperty("user","root")
        addDataSourceProperty("password","test")
        addDataSourceProperty("characterEncoding", "utf8")
        addDataSourceProperty("useUnicode", "true")
        isAutoCommit = false
        connectionInitSql = "SELECT 1"

        connectionTimeout = 10000 // connection is timeout and throw SQLException after 10 seconds.
        idleTimeout = 60000 // Idled connection in pool shutdown after 1 minutes
        maxLifetime = 120000 // Connection in pool shutdown after 2 minutes
        // A constant indicating that dirty reads, non-repeatable reads and phantom reads are prevented.
        //  default is REPEATABLE READ
        // transactionIsolation = "TRANSACTION_SERIALIZABLE"
        validate()
    }
    return HikariDataSource(config)
}
