package com.example.springredis.common.support

import com.example.springredis.common.exception.ErrorCode
import com.example.springredis.common.exception.Exception
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component

@Component
@EnableAsync
class AsyncExecutor {
    @Async
    fun runAsync(action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            throw Exception(ErrorCode.ASYNC_EXECUTION_FAILED)
        }
    }
}
