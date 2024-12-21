package com.example.springredis.common.model

data class ValueWithTtl<T>(
    val value: T,
    val ttl: Long,
)
