package com.kentrino

import com.google.common.base.CaseFormat

private fun assertLoad(key: String): String {
    val propertyName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key)
    println(System.getProperty(propertyName))
    return (System.getenv(key) ?: System.getProperty(propertyName)) ?: throw Exception("You have to define $key")
}

data class Config(
        val port: Int = assertLoad("PORT").toInt(),
        val gitHubWebhookSecret: String = assertLoad("GIT_HUB_WEBHOOK_SECRET"),
        val youtrack: YoutrackConfig = YoutrackConfig()
)

data class YoutrackConfig(
        val authorizationToken: String = assertLoad("YOUTRACK_AUTHORIZATION_TOKEN"),
        val subDomainName: String = assertLoad("YOUTRACK_SUBDOMAIN_NAME")
)
