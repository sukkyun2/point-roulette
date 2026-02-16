package org.example.roulette.api.common.app

import java.util.concurrent.TimeUnit

interface LockExecutor {
    fun <T> runWithLock(
        lockName: String,
        waitTime: Long = 30L,
        leaseTime: Long = 10L,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        function: () -> T,
    ): T
}
