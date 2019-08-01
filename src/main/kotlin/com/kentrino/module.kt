package com.kentrino

import com.kentrino.db.UfoSightings
import com.kentrino.db.createHikariDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.Module
import org.koin.dsl.module


fun module(): Module = module(createdAtStart = true) {
    single<Database> {
        val dataSource = createHikariDataSource()
        val connection = Database.connect(dataSource)
        runBlocking {
            transaction(connection) {
                SchemaUtils.createMissingTablesAndColumns(UfoSightings)
            }
        }
        connection
    }
}
