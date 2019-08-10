package com.kentrino

import io.ktor.client.HttpClient
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.URLProtocol
import org.koin.core.KoinComponent
import org.koin.core.inject

data class Project(
        val shortName: String?,
        val name: String?,
        val id: String?,
        // Project
        val `$type`: String?
)

data class CreateIssue(
        val project: Project,
        val summary: String,
        val description: String
)

data class Issue(
        val id: String,
        // Issue
        val `$type`: String
)

data class CreateIssueComment(
        val text: String,
        val usesMarkdown: Boolean = true
)

data class IssueComment(
        val id: String,
        // IssueComment
        val `$type`: String
)

class YoutrackApi: KoinComponent {
    private val client by inject<HttpClient>()
    private val config by inject<Config>()
    private val json = defaultSerializer()

    suspend fun listProjects(): List<Project> = client.get {
        youtrackDefault("/youtrack/api/admin/projects?fields=id,name,shortName")
    }

    suspend fun getProject(name: String): List<Project> = client.get {
        youtrackDefault("/youtrack/api/admin/projects?fields=id,name,shortName&query=$name")
    }

    // idReadable is like PROJECT-43
    suspend fun findIssue(idReadable: String): List<Issue> = client.get {
        youtrackDefault("/youtrack/api/issues?query=issue%20id:$idReadable")
    }

    suspend fun createIssue(createIssue: CreateIssue): Issue = client.post {
        youtrackDefault("/youtrack/api/issues")
        // NOTE: body = createIssue does not works!
        body = json.write(createIssue)
    }

    suspend fun createIssueComment(issueId: String, text: String): IssueComment = client.post {
        youtrackDefault("/youtrack/api/issues/$issueId/comments")
        body = json.write(CreateIssueComment(text = text))
    }

    private fun HttpRequestBuilder.youtrackDefault(path: String) {
        url {
            protocol = URLProtocol.HTTPS
            host = "${config.youtrack.subDomainName}.myjetbrains.com"
            encodedPath = path
        }
        header("Authorization", "Bearer ${config.youtrack.authorizationToken}")
        // NOTE: Adding Content-Type produce Content-Length: 2,
        //    then the server respond with 400
    }
}
