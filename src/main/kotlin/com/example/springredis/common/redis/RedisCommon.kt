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

    fun <T> addToSortedSet(
        key: String,
        value: T,
        score: Double,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForZSet().add(key, jsonValue, score)
    }

    fun <T> rangeByScore(
        key: String,
        minScore: Double,
        maxScore: Double,
        clazz: Class<T>,
    ): Set<T> {
        val jsonValues = template.opsForZSet().rangeByScore(key, minScore, maxScore)
        return jsonValues?.map { objectMapper.readValue(it, clazz) }?.toSet() ?: emptySet()
    }

    fun <T> topNFromSortedSet(
        key: String,
        n: Long,
        clazz: Class<T>,
    ): Set<T> {
        val jsonValues = template.opsForZSet().reverseRange(key, 0, n - 1)
        return jsonValues?.map { objectMapper.readValue(it, clazz) }?.toSet() ?: emptySet()
    }

    fun <T> addToListLeft(
        key: String,
        value: T,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForList().leftPush(key, jsonValue)
    }

    fun <T> addToListRight(
        key: String,
        value: T,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForList().rightPush(key, jsonValue)
    }

    fun <T> getList(
        key: String,
        clazz: Class<T>,
    ): List<T> {
        val jsonValues = template.opsForList().range(key, 0, -1)
        return jsonValues?.map { objectMapper.readValue(it, clazz) } ?: emptyList()
    }

    fun <T> removeFromList(
        key: String,
        value: T,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForList().remove(key, 1, jsonValue)
    }
}
