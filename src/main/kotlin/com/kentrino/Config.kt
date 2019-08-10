package com.kentrino

data class Config(
        val token: String = System.getenv("YOUTRACK_AUTHORIZATION_TOKEN")!!
)
