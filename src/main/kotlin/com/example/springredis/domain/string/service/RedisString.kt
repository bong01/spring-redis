package com.example.springredis.domain.string.service

import com.example.springredis.common.redis.RedisCommon
import com.example.springredis.domain.string.model.StringModel
import com.example.springredis.domain.string.model.request.MultiStringRequest
import com.example.springredis.domain.string.model.request.StringRequest
import com.example.springredis.domain.string.model.response.StringResponse
import org.springframework.stereotype.Service

@Service
class RedisString(
    private val redis: RedisCommon,
) {
    fun set(request: StringRequest) {
        val key = request.key
        val newModel = StringModel(key, request.value)

        redis.setData(key, newModel)
    }

    fun get(key: String): StringResponse {
        val result = redis.getData(key, StringModel::class.java)

        return result?.let { StringResponse(listOf(it)) } ?: StringResponse(emptyList())
    }

    fun multiSet(request: MultiStringRequest) {
        val dataMap =
            request.values
                .mapIndexed { index, value ->
                    val key = "${request.key}:${index + 1}"
                    key to StringModel(key, value)
                }.toMap()

        return redis.multiSetData(dataMap)
    }
}
