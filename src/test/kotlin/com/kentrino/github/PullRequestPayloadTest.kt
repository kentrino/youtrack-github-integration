package com.kentrino.github

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kentrino.rule.InjectDependencies
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class PullRequestPayloadTest : KoinTest {
    @JvmField
    @Rule
    val injectDependencies = InjectDependencies()

    @Test
    fun deserialize() {
        val objectMapper by inject<ObjectMapper>()
        val payload = javaClass.classLoader.getResourceAsStream("json/close.json")!!.use {
            objectMapper.readValue<PullRequestPayload>(it.readAllBytes())
        }
        assertEquals("test-1", payload.pullRequest.head.ref)
    }
}
