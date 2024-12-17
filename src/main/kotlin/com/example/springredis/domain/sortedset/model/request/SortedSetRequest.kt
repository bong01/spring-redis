package com.example.springredis.domain.sortedset.model.request

data class SortedSetRequest(
    val key: String,
    val value: String,
    val score: Double,
)
