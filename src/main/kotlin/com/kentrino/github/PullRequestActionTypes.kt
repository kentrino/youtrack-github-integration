package com.kentrino.github

import com.google.common.base.CaseFormat
import jp.justincase.jackson.kotlin.textual.Textual


sealed class PullRequestActionTypes {
    object Opened : PullRequestActionTypes()
    object Closed: PullRequestActionTypes()

    val value: String
        get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this::class.simpleName!!)

    companion object : Textual<PullRequestActionTypes> {
        private val values = PullRequestActionTypes::class
                .sealedSubclasses
                .map { it.objectInstance!! }
        private val valueMap = values.associateBy(PullRequestActionTypes::value)


        override val PullRequestActionTypes.text
            get() = value

        override fun fromText(value: String): PullRequestActionTypes = valueMap[value] ?: error("no enum")
    }
}
