package com.example.springredis.domain.hashes.controller

import com.example.springredis.domain.hashes.model.reqeust.HashRequest
import com.example.springredis.domain.hashes.service.RedisHash
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HashController(
    private val redis: RedisHash,
) {
    @PostMapping("/hashes:put")
    fun putInHash(
        @RequestBody request: HashRequest,
    ) = redis.putInHash(request.key, request.field, request.value)

    @GetMapping("/hashes")
    fun getFromHash(
        @RequestParam key: String,
        @RequestParam field: String,
    ) = redis.getFromHash(key, field)
}
