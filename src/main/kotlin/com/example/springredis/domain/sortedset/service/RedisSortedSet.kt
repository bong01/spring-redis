package com.example.springredis.domain.sortedset.service

import com.example.springredis.common.redis.RedisCommon
import com.example.springredis.domain.sortedset.model.SortedSet
import com.example.springredis.domain.sortedset.model.request.SortedSetRequest
import org.springframework.stereotype.Service

@Service
class RedisSortedSet(
    private val redis: RedisCommon,
) {
    fun setSortedSet(request: SortedSetRequest) {
        val model = SortedSet(request.value, request.score)
        redis.addToSortedSet(request.key, model, request.score)
    }

    fun getSetDataByRange(
        key: String,
        minScore: Double,
        maxScore: Double,
    ): Set<SortedSet> = redis.rangeByScore(key, minScore, maxScore, SortedSet::class.java)

    fun getTopN(
        key: String,
        n: Long,
    ): List<SortedSet> = redis.topNFromSortedSet(key, n, SortedSet::class.java)
}
