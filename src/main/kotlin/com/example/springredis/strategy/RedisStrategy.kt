package com.example.springredis.strategy

import com.example.springredis.common.redis.RedisCommon
import com.example.springredis.common.support.AsyncExecutor
import com.example.springredis.domain.string.model.StringModel
import org.springframework.stereotype.Component
import kotlin.math.exp
import kotlin.random.Random

@Component
class RedisStrategy(
    private val redis: RedisCommon,
    private val asyncExecutor: AsyncExecutor,
) {
    fun simpleStrategy(key: String): StringModel =
        redis.getData(key, StringModel::class.java)
            ?: StringModel(key, "new db").also {
                redis.setData(key, it)
            }

    fun perStrategy(key: String): StringModel {
        try {
            val valueWithTtl = redis.getValueWithTtl(key, StringModel::class.java)
            valueWithTtl.let {
                asyncPerStrategy(key, it.ttl)
                return it.value
            }
        } catch (e: Exception) {
            return StringModel(key, "new db").also {
                redis.setData(key, it)
            }
        }
    }

    private fun asyncPerStrategy(
        key: String,
        remainTtl: Long,
    ) {
        asyncExecutor.runAsync {
            val probability = calculateProbability(remainTtl)

            if (Random.nextDouble() < probability) {
                val fromDb = StringModel(key, "db from")
                redis.setData(key, fromDb)
            }
        }
    }

    private fun calculateProbability(remainTtl: Long): Double {
        val base = 0.5
        val decayRate = 0.1

        return base * exp(-decayRate * remainTtl)
    }

    fun luaScript(
        key1: String,
        key2: String,
        newKey: String,
    ) {
        redis.sumTwoKeyAndRenew(key1, key2, newKey)
    }
}
