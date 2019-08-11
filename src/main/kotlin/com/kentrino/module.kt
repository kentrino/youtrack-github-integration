package com.kentrino

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kentrino.ext.jackson.configure
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.util.KtorExperimentalAPI
import org.koin.core.module.Module
import org.koin.dsl.module
import org.slf4j.Logger


@KtorExperimentalAPI
fun module(config: Config, logger: Logger): Module = module(createdAtStart = true) {
    /*
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
    */
    single { logger }

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

    single { YoutrackApi() }
    single { config }
    single { PayloadHandler() }
    single { YoutrackService() }

    single {
        jacksonObjectMapper().apply {
            configure()
        }
    }
}
