package com.kentrino.rule

import com.kentrino.module
import com.kentrino.util.testConfig
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.slf4j.LoggerFactory

@Suppress("EXPERIMENTAL_API_USAGE")
open class InjectDependencies : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                before()
                try {
                    base.evaluate()
                } finally {
                    after()
                }
            }
        }
    }

    private fun before() {
        GlobalContext.getOrNull() ?: startKoin {
            modules(module(testConfig, LoggerFactory.getLogger(InjectDependencies::class.java)))
        }
    }

    private fun after() {
        stopKoin()
    }
}
