@file:JvmName("Application")
package com.kentrino

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.readValue
import com.kentrino.ext.jackson.configure
import com.kentrino.github.GitHubEventTypes
import com.kentrino.github.PullRequestPayload
import com.kentrino.github.github
import com.kentrino.github.githubHeaders
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DoubleReceive
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import org.koin.Logger.SLF4JLogger
import org.koin.core.context.startKoin
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
const val gitHubWebhookAuth = "githubAuth"

@KtorExperimentalAPI
fun Application.injectDependencies(config: Config) {
    val authorizedCredential = UserPasswordCredential(name = "kentrino", password = config.youtrack.authorizationToken)

    install(CallLogging) {
        level = Level.INFO
    }

    install(Koin) {
        SLF4JLogger()
        startKoin {
            modules(module(config))
        }
    }

    install(ContentNegotiation) {
        jackson {
            configure()
        }
    }

    install(DoubleReceive) {
        receiveEntireContent = true
    }

    install(Authentication) {
        github(name = gitHubWebhookAuth) {
            secret = config.gitHubWebhookSecret
        }

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

@KtorExperimentalAPI
fun Application.main() {
    routing {
        val api by inject<YoutrackApi>()
        val objectMapper by inject<ObjectMapper>()
        val payloadHandler by inject<PayloadHandler>()

        authenticate(gitHubWebhookAuth) {
            post<ByteArray>("/webhooks/github") {
                val header = call.githubHeaders()
                val payloadJson = String(it)
                when (header.event) {
                    GitHubEventTypes.pull_request -> {
                        val p = objectMapper.readValue<PullRequestPayload>(payloadJson)
                        payloadHandler.handle(p)
                    }
                    GitHubEventTypes.push -> {  }
                }
                call.respondText("")
            }
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
