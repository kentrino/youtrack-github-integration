package com.kentrino

private fun assertLoad(key: String): String {
    return System.getenv(key) ?: System.getProperty(key) ?: throw Exception("You have to define $key")
}

data class Config(
        val youtrack: YoutrackConfig = YoutrackConfig()
)

data class YoutrackConfig(
        val authorizationToken: String = assertLoad("YOUTRACK_AUTHORIZATION_TOKEN"),
        val subDomainName: String = assertLoad("YOUTRACK_SUBDOMAIN_NAME")
)
