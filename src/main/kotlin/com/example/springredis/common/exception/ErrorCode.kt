package com.example.springredis.common.exception

enum class ErrorCode(
    val code: Int,
    val message: String,
) {
    REDIS_VALUE_NOT_FOUND(code = 100, message = "redis value not found"),
    REDIS_BIT_NOT_INITIALIZED(code = 101, message = "redis bit flag not initialized"),
    REDIS_TTL_NOT_FOUND(code = 102, message = "redis ttl not found"),

    ASYNC_EXECUTION_FAILED(code = 200, message = "async execution failed"),
}
