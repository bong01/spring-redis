package com.example.springredis.domain.hashes.service

import com.example.springredis.common.redis.RedisCommon
import com.example.springredis.domain.hashes.model.HashModel
import org.springframework.stereotype.Service

@Service
class RedisHash(
    private val redis: RedisCommon,
) {
    fun putInHash(
        key: String,
        field: String,
        value: String,
    ) {
        val model = HashModel(value)
        redis.putInHash(key, field, model)
    }

    fun getFromHash(
        key: String,
        field: String,
    ) = redis.getFromHash(key, field, HashModel::class.java)
}
