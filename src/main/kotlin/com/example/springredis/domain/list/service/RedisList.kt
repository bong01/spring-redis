package com.example.springredis.domain.list.service

import com.example.springredis.common.redis.RedisCommon
import com.example.springredis.domain.list.model.ListModel
import org.springframework.stereotype.Service

@Service
class RedisList(
    private val redis: RedisCommon,
) {
    fun addToListLeft(
        key: String,
        value: String,
    ) {
        val model = ListModel(value)
        redis.addToListLeft(key, model)
    }

    fun addToListRight(
        key: String,
        value: String,
    ) {
        val model = ListModel(value)
        redis.addToListRight(key, model)
    }

    fun getAllData(key: String): List<ListModel> = redis.getAllList(key, ListModel::class.java)
}
