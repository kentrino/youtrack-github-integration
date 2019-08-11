package com.kentrino.ext.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import jp.justincase.jackson.kotlin.textual.codec.TextualModule

fun ObjectMapper.configure() {
    registerModule(TextualModule())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
}
