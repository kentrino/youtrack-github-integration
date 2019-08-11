package com.kentrino.github

import io.ktor.application.ApplicationCall
import io.ktor.application.call

data class GitHubHeaders(
        val event: EventType?,
        val delivery: String,
        val signature: String
)

fun ApplicationCall.githubHeaders(): GitHubHeaders {
    return GitHubHeaders(
            event = request.headers["X-GitHub-Event"]?.let { EventType.valueOf(it) },
            delivery = request.headers["X-GitHub-Delivery"]!!,
            signature = request.headers["X-Hub-Signature"]!!
    )
}
