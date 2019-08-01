package com.kentrino

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main(args: Array<String>) {
    embeddedServer(
            factory = Netty,
            host = "0.0.0.0",
            port = 10080,
            configure = {
                callGroupSize = 200
            },
            module = {
                this.main()
            }
    ).start()
}

fun Application.main() {
    routing {
        get("/") {
            call.respondText("Hi!", contentType = ContentType.Text.Plain)
        }
    }
}
