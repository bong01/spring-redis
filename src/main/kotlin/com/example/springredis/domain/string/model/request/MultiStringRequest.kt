package com.example.springredis.domain.string.model.request

data class MultiStringRequest(
    val key: String,
    val values: Collection<String>,
)
