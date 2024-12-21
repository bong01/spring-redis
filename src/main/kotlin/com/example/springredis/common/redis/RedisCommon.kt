package com.example.springredis.common.redis

import com.example.springredis.common.exception.ErrorCode
import com.example.springredis.common.exception.Exception
import com.example.springredis.common.model.ValueWithTtl
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.connection.StringRedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
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
    ): List<T> {
        val jsonValues = template.opsForZSet().reverseRange(key, 0, n - 1)
        return jsonValues?.map { objectMapper.readValue(it, clazz) } ?: emptyList()
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

    fun <T> getAllList(
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

    fun <T> putInHash(
        key: String,
        hashKey: String,
        value: T,
    ) {
        val jsonValue = objectMapper.writeValueAsString(value)
        template.opsForHash<String, String>().put(key, hashKey, jsonValue)
    }

    fun <T> getFromHash(
        key: String,
        hashKey: String,
        clazz: Class<T>,
    ): T? {
        val jsonValue = template.opsForHash<String, String>().get(key, hashKey)
        return jsonValue?.let { objectMapper.readValue(it, clazz) }
    }

    fun removeFromHash(
        key: String,
        hashKey: String,
    ) {
        template.opsForHash<String, String>().delete(key, hashKey)
    }

    fun setBit(
        key: String,
        offset: Long,
        value: Boolean,
    ) {
        template.opsForValue().setBit(key, offset, value)
    }

    fun getBit(
        key: String,
        offset: Long,
    ): Boolean = template.opsForValue().getBit(key, offset) ?: throw Exception(ErrorCode.REDIS_BIT_NOT_INITIALIZED)

    fun <T> getValueWithTtl(
        key: String,
        clazz: Class<T>,
    ): ValueWithTtl<T> {
        val results =
            template.executePipelined { connection ->
                (connection as StringRedisConnection).apply {
                    get(key)
                    ttl(key)
                }
                null
            }

        val value =
            results[0]?.let { objectMapper.readValue(it as String, clazz) }
                ?: throw Exception(ErrorCode.REDIS_VALUE_NOT_FOUND)
        val ttl = results[1] as? Long ?: throw Exception(ErrorCode.REDIS_TTL_NOT_FOUND)

        return ValueWithTtl(value, ttl)
    }

    fun sumTwoKeyAndRenew(
        key1: String,
        key2: String,
        resultKey: String,
    ): Long =
        DefaultRedisScript<Long>()
            .apply {
                setLocation(ClassPathResource("/lua/newKey.lua"))
                setResultType(Long::class.java)
            }.let { script ->
                template.execute(script, listOf(key1, key2, resultKey))
            }
}
