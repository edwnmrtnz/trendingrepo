package com.edwnmrtnz.trendingrepo.core.domain

data class GithubRepo(
    val avatar: String,
    val username: String,
    val repoName: String,
    val description: String?,
    val primaryLanguage: String,
    val starCount: Int
)
