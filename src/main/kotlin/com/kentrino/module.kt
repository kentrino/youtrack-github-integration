package com.kentrino

import com.kentrino.db.UfoSightings
import com.kentrino.db.createHikariDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.module.Module
import org.koin.dsl.module


@KtorExperimentalAPI
fun module(config: Config): Module = module(createdAtStart = true) {
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

    single {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                }
            }
            engine {
                maxConnectionsCount = 1000

                endpoint {
                    /**
                     * Maximum number of requests for a specific endpoint route.
                     */
                    maxConnectionsPerRoute = 100
                    // Max size of scheduled requests per connection(pipeline queue size).
                    pipelineMaxSize = 20
                    /**
                     * Max number of milliseconds to keep iddle connection alive.
                     */
                    keepAliveTime = 5000
                    /**
                     * Number of milliseconds to wait trying to connect to the server.
                     */
                    connectTimeout = 5000
                    /**
                     * Maximum number of attempts for retrying a connection.
                     */
                    connectRetryAttempts = 5
                }
            }
        }
    }

    single {
        YoutrackApi()
    }

    single {
        config
    }
}
