package com.kentrino.github

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.request.header
import io.ktor.request.receive
import io.ktor.response.respond
import org.apache.commons.codec.digest.HmacUtils


class GitHubAuthenticationProvider internal constructor(
        configuration: Configuration
) : AuthenticationProvider(configuration) {
    var secret: String = configuration.secret!!

    class Configuration(name: String?): AuthenticationProvider.Configuration(name) {
        var secret: String? = null
    }
}

suspend fun gitHubWebhookAuthenticate(call: ApplicationCall, credential: GitHubWebhookCredential, secret: String): Principal? {
    val payload = call.receive<ByteArray>()
    val encoded = "sha1=" + HmacUtils.hmacSha1Hex(secret.toByteArray(), payload)
    return if (encoded == credential.signature) {
        GitHubWebhookAuthorizedPrincipal
    } else {
        null
    }
}

fun Authentication.Configuration.github(
        name: String? = null,
        configure: GitHubAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = GitHubAuthenticationProvider(GitHubAuthenticationProvider.Configuration(name).apply(configure))
    val secret = provider.secret

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val credentials = call.request.header("X-Hub-Signature")?.let { GitHubWebhookCredential(it) }
        // NOTE: principal = authenticated credential
        val principal = credentials?.let { gitHubWebhookAuthenticate(call, it, secret) }

        val cause = when {
            credentials == null -> AuthenticationFailedCause.NoCredentials
            principal == null -> AuthenticationFailedCause.InvalidCredentials
            else -> null
        }

        if (cause != null) {
            context.challenge(gitHubWebhookAuthenticationChallengeKey, cause) {
                call.respond(UnauthorizedResponse())
                it.complete()
            }
        }
        if (principal != null) {
            context.principal(principal)
        }
    }

    register(provider)
}

data class GitHubWebhookCredential(val signature: String): Credential

object GitHubWebhookAuthorizedPrincipal: Principal

private val gitHubWebhookAuthenticationChallengeKey: Any = "GitHubWebhookAuth"
