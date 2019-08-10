package com.kentrino

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import org.koin.core.KoinComponent
import org.koin.core.inject

data class Project(
        val shortName: String?,
        val name: String?,
        val id: String?,
        val `$type`: String?
)

class YoutrackApi: KoinComponent {
    private val client by inject<HttpClient>()
    private val config by inject<Config>()

    suspend fun listProjects(): List<Project> {
        return client.get {
            youtrackDefault()
        }
    }

    suspend fun getProject(name: String): List<Project> {
        return client.get {
            youtrackDefault()
            url {
                encodedPath = "/youtrack/api/admin/projects?fields=id,name,shortName&query=$name"
            }
        }
    }

    private fun HttpRequestBuilder.youtrackDefault() {
        url {
            protocol = URLProtocol.HTTPS
            host = "${config.youtrack.subDomainName}.myjetbrains.com"
            encodedPath = "/youtrack/api/admin/projects?fields=id,name,shortName"
        }
        header("Authorization", "Bearer ${config.youtrack.authorizationToken}")
        // NOTE: Adding Content-Type produce Content-Length: 2,
        //    then the server respond with 400
    }
}
