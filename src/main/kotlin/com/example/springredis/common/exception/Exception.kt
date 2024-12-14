package com.example.springredis.common.exception

open class Exception(
    errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
