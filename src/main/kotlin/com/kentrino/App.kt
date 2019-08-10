package com.kentrino

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import org.koin.Logger.SLF4JLogger
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


@KtorExperimentalAPI
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

@KtorExperimentalAPI
fun Application.injectDependencies() {
    val config = Config()

    install(Koin) {
        SLF4JLogger()
        modules(module(config))
    }
    install(ContentNegotiation) {
        jackson {}
    }
}

fun Application.main() {
    routing {
        // val connection by inject<Database>()
        val api by inject<YoutrackApi>()

        get("/") {
            val project = api.getProject("kentrnio")
            val issue = api.createIssue(CreateIssue(
                    project = project.first(),
                    summary = "test",
                    description = "あああああああああああああああ"
            ))
            val res = api.createIssueComment(issue.id, """
            [阿部寛](http://abehiroshi.la.coocan.jp/top.htm)
            """.trimIndent())
            call.respond(res)
        }
    }
}
