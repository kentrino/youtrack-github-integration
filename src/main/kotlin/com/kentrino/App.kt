package com.kentrino

import com.kentrino.db.UfoSightings
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.koin.Logger.SLF4JLogger
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


fun main(args: Array<String>) {
    embeddedServer(
            factory = Netty,
            host = "0.0.0.0",
            port = 10080,
            configure = {
                callGroupSize = 200
            },
            module = {
                injectDependencies()
                main()
            }
    ).start()
}

fun Application.injectDependencies() {
    install(Koin) {
        SLF4JLogger()
        modules(module())
    }
}

fun Application.main() {
    routing {
        val connection by inject<Database>()

        get("/") {
            call.respondText("Hi!", contentType = ContentType.Text.Plain)
        }
    }
}
