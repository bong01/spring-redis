package com.example.springredis.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    REDIS_VALUE_NOT_FOUND(code = 100, message = "redis value not found"),
}
