package com.kentrino.github

data class PullRequestPayload(
        val action: PullRequestActionTypes,
        val number: Int,
        val pullRequest: PullRequest
) {
    // ref: branch name
    data class Head(val ref: String)

    data class PullRequest(
        val user: User,
        val head: Head,
        val htmlUrl: String,
        val title: String,
        val merged: Boolean,
        val mergeCommitSha: String?
    ) {
        data class User(
                // name
                val login: String,
                val htmlUrl: String
        )
    }
}
