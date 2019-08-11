package com.kentrino

import org.junit.Test
import org.koin.test.KoinTest
import kotlin.test.assertEquals

class IssueMatcherTest: KoinTest {
    @Test
    fun test() {
        issueMatcher("FRONTEND-1")
        val testCases: Map<String, String?> = mapOf(
                "feature/FRONTEND-1" to "FRONTEND-1",
                "feature/FRONTEND-1-foo" to "FRONTEND-1",
                "FRONTEND-1" to "FRONTEND-1",
                "FRONTEND-1-foo" to "FRONTEND-1",
                "frontend-1" to null
        )
        testCases.forEach {
            assertEquals(it.value, issueMatcher(it.key))
        }
    }
}
