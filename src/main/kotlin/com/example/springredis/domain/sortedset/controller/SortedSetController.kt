package com.example.springredis.domain.sortedset.controller

import com.example.springredis.domain.sortedset.model.SortedSet
import com.example.springredis.domain.sortedset.model.request.SortedSetRequest
import com.example.springredis.domain.sortedset.service.RedisSortedSet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sorted-sets")
class SortedSetController(
    private val redis: RedisSortedSet,
) {
    @PostMapping
    fun setSortedSet(
        @RequestBody request: SortedSetRequest,
    ) = redis.setSortedSet(request)

    @GetMapping("/{key}")
    fun getSortedSetByRange(
        @PathVariable key: String,
        @RequestParam minScore: Double,
        @RequestParam maxScore: Double,
    ): Set<SortedSet> = redis.getSetDataByRange(key, minScore, maxScore)

    @GetMapping("/top")
    fun getTopN(
        @RequestParam key: String,
        @RequestParam n: Long,
    ): List<SortedSet> = redis.getTopN(key, n)
}
