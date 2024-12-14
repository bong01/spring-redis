package com.example.springredis.domain.string.controller

import com.example.springredis.domain.string.model.request.MultiStringRequest
import com.example.springredis.domain.string.model.request.StringRequest
import com.example.springredis.domain.string.model.response.StringResponse
import com.example.springredis.domain.string.service.RedisString
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/strings")
class StringController(
    private val redis: RedisString,
) {
    @PostMapping
    fun setString(
        @RequestBody request: StringRequest,
    ) = redis.set(request)

    @GetMapping("/{key}")
    fun getString(
        @PathVariable key: String,
    ): StringResponse = redis.get(key)

    @PostMapping("/bulk")
    fun multiSetString(
        @RequestBody request: MultiStringRequest,
    ) = redis.multiSet(request)
}
