package com.kentrino.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.kentrino.rule.InjectDependencies
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import com.fasterxml.jackson.module.kotlin.readValue
import org.koin.test.inject
import kotlin.test.assertEquals

class EnumTest : KoinTest {
    @JvmField
    @Rule
    val injectDependencies = InjectDependencies()

    @Test
    fun `serialize - deserialize` () {
        val objectMapper by inject<ObjectMapper>()
        val json = """
        {
            "foo": "foo_bar"
        }
        """.trimIndent()
        val parsed = objectMapper.readValue<Container>(json)
        assertEquals(Foo.foo_bar, parsed.foo)
    }

    private enum class Foo {
        foo_bar,
        baz_bar
    }

    private data class Container(val foo: Foo)
}
