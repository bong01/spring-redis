package com.example.springredis.domain.hashes.model.reqeust

data class HashRequest(
    val key: String,
    val field: String,
    val value: String,
)
