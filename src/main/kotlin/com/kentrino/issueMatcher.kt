package com.kentrino

private const val issueIdGroupKey = "issueId"
private val issueMatcher = Regex("^[a-z0-9A-Z]*?/?(?<$issueIdGroupKey>[A-Z]+-[0-9]+).*$")

fun issueMatcher(branchName: String): String? = issueMatcher.find(branchName)?.groups?.get(issueIdGroupKey)?.value
