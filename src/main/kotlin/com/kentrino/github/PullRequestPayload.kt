package com.kentrino.github

data class PullRequestPayload(
        val action: PullRequestActionTypes,
        val number: Int,
        val pullRequest: PullRequest,
        val head: Head
) {
    // ref: branch name
    data class Head(val ref: String)

    data class PullRequest(
        val user: User
    ) {
        data class User(
                val login: String,
                val htmlUrl: String
        )
    }
}
