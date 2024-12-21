package com.example.springredis.domain.list.controller

import com.example.springredis.domain.list.model.request.ListRequest
import com.example.springredis.domain.list.service.RedisList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ListController(
    private val redis: RedisList,
) {
    @PostMapping("/lists:append")
    fun setNewValueToRight(
        @RequestBody request: ListRequest,
    ) {
        redis.addToListRight(request.key, request.value)
    }

    @PostMapping("/lists:prepend")
    fun setNewValueToLeft(
        @RequestBody request: ListRequest,
    ) {
        redis.addToListLeft(request.key, request.value)
    }

    @GetMapping("/lists")
    fun getAll(
        @RequestParam key: String,
    ) = redis.getAllData(key)
}
