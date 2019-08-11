package com.kentrino

import com.kentrino.github.PullRequestActionTypes
import com.kentrino.github.PullRequestPayload
import org.koin.core.KoinComponent
import org.koin.core.inject

class PayloadHandler : KoinComponent {
    private val youtrackService by inject<YoutrackService>()

    suspend fun handle(pullRequestPayload: PullRequestPayload) {
        val issueId = issueMatcher(pullRequestPayload.pullRequest.head.ref) ?: return
        val comment = when (val action = pullRequestPayload.action) {
            is PullRequestActionTypes.Opened -> comment(action, pullRequestPayload)
            is PullRequestActionTypes.Closed -> comment(action, pullRequestPayload)
        }
        youtrackService.addIssueComment(issueId, comment)
    }

    private fun comment(action: PullRequestActionTypes.Opened, pullRequestPayload: PullRequestPayload): String {

        return "pull request [${pullRequestPayload.pullRequest.title}](${pullRequestPayload.pullRequest.htmlUrl})" +
                " opened by [${pullRequestPayload.pullRequest.user.login}](${pullRequestPayload.pullRequest.user.htmlUrl})"
    }

    private fun comment(action: PullRequestActionTypes.Closed, pullRequestPayload: PullRequestPayload): String =
            when (pullRequestPayload.pullRequest.merged) {
                true -> "pull request [${pullRequestPayload.pullRequest.title}](${pullRequestPayload.pullRequest.htmlUrl}) merged"
                false -> "pull request [${pullRequestPayload.pullRequest.title}](${pullRequestPayload.pullRequest.htmlUrl}) closed"
            }
}
