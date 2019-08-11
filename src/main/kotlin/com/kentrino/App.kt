@file:JvmName("Application")
package com.kentrino

import com.kentrino.github.githubHeaders
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
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
    val config = Config()

    embeddedServer(
            factory = Netty,
            host = "0.0.0.0",
            port = config.port,
            configure = {
                callGroupSize = 200
            },
            module = {
                injectDependencies(config)
                main()
            }
    ).start()
}

const val basicAuth = "basicAuth"

@KtorExperimentalAPI
fun Application.injectDependencies(config: Config) {
    val authorizedCredential = UserPasswordCredential(name = "kentrino", password = config.youtrack.authorizationToken)

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

        post<ByteArray>("/webhooks/github") {
            val header = call.githubHeaders()
            println(header)
            println(String(it))
            call.respondText("Hi!")
        }

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
