package com.example.springredis.common.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisCommon(
    private val template: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    @Value("\${spring.data.redis.default-ttl}")
    private val defaultTtl: Duration,
) {
    fun <T> getData(
        key: String,
        clazz: Class<T>,
    ): T? {
        val jsonValue = template.opsForValue().get(key)
        return jsonValue?.let { objectMapper.readValue(it, clazz) }
    }

    fun <T> setData(
        key: String,
        value: T,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForValue().set(key, jsonValue, defaultTtl)
    }

    fun <T> multiSetData(data: Map<String, T>) {
        val jsonMap = data.mapValues { objectMapper.writeValueAsString(it.value) }
        template.opsForValue().multiSet(jsonMap)
    }
}
