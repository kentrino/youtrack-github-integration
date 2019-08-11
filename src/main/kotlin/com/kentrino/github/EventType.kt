package com.kentrino.github

import com.google.common.base.CaseFormat

sealed class EventType {
    object PullRequest : EventType()
    object Push: EventType()

    val raw: String
        get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this::class.simpleName!!)

    companion object {
        private val values = EventType::class
                .sealedSubclasses
                .map { it.objectInstance!! }

        private val nameMap = values.associateBy(EventType::raw)

        fun valueOf(str: String): EventType? {
            return nameMap[str]
        }
    }
}
