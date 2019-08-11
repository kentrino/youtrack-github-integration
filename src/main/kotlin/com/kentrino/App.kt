@file:JvmName("Application")
package com.kentrino

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import org.koin.Logger.SLF4JLogger
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.slf4j.event.Level
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


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

const val basicAuth = "basicAuth"
val authorizedCredential = UserPasswordCredential(name = "kentrino", password = "the-best-password-ever")

@KtorExperimentalAPI
fun Application.injectDependencies() {
    val config = Config()

    install(CallLogging) {
        level = Level.INFO
    }

    install(Koin) {
        SLF4JLogger()
        modules(module(config))
    }
    install(ContentNegotiation) {
        jackson {}
    }

    install(Authentication) {
        basic(name = basicAuth) {
            realm = "App"
            validate { credentials ->
                if (credentials == authorizedCredential) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.main() {
    routing {
        val api by inject<YoutrackApi>()

        authenticate(basicAuth) {
            post("/{issue}/abe") {
                val issueId = call.parameters["issue"] ?: throw Exception("you must specify issue")
                /*
                val project = api.getProject("TEST")
                val issue = api.createIssue(CreateIssue(
                        project = project.first(),
                        summary = "test",
                        description = "てすと"
                ))
                */
                val issue = api.findIssue(issueId).first()
                val res = api.createIssueComment(issue.id, """
                ${ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)}
                [阿部寛](http://abehiroshi.la.coocan.jp/top.htm)
                """.trimIndent())
                call.respond(res)
            }
        }
    }
}
