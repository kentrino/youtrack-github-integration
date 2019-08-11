package com.kentrino

import com.kentrino.github.PullRequestPayload
import org.koin.core.KoinComponent

class PayloadHandler : KoinComponent {
    fun handle(pullRequestPayload: PullRequestPayload) {
        println(pullRequestPayload)
    }
}
