package com.kentrino.util

import com.kentrino.Config
import com.kentrino.YoutrackConfig

val testConfig = Config(
        port = 10080,
        gitHubWebhookSecret = "",
        youtrack = YoutrackConfig("", "")
)
