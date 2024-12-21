package com.example.springredis.strategy

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/strategies")
class StrategyController(
    private val strategy: RedisStrategy,
) {
    @PostMapping("/lua-script")
    fun luaScript(
        @RequestParam key1: String,
        @RequestParam key2: String,
        @RequestParam newKey: String,
    ) {
        strategy.luaScript(key1, key2, newKey)
    }
}
