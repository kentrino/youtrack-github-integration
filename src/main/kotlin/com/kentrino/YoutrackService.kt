package com.kentrino

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.Logger

class YoutrackService: KoinComponent {
    private val api by inject<YoutrackApi>()
    private val logger by inject<Logger>()

    suspend fun addIssueComment(issueId: String, comment: String) {
        val issue = api.findIssue(issueId).first()
        val created = api.createIssueComment(issue.id, comment)
        logger.info("issue id: $issueId")
        logger.info(comment)
        logger.info("Issue created successfully: ${created.id}")
    }
}
