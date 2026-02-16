package org.example.roulette.api.common.infra

import org.example.roulette.api.common.app.LockExecutor
import org.example.roulette.api.common.app.LockTimeoutException
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
class RedisLockExecutor(
    private val redissonClient: RedissonClient,
) : LockExecutor {
    private val log = LoggerFactory.getLogger(RedisLockExecutor::class.java)

    @Transactional(propagation = Propagation.NEVER)
    override fun <T> runWithLock(
        lockName: String,
        waitTime: Long,
        leaseTime: Long,
        timeUnit: TimeUnit,
        function: () -> T,
    ): T {
        val rLock = redissonClient.getLock(lockName)
        log.debug("Redis 락 획득 시도: $lockName")

        try {
            if (!rLock.tryLock(waitTime, leaseTime, timeUnit)) {
                throw LockTimeoutException(lockName, waitTime)
            }

            log.debug("Redis 락 획득 성공: $lockName")
            return runInTransaction(function)
        } catch (e: Exception) {
            log.error("Redis 락 실행 중 오류 발생: $lockName", e)
            throw e
        } finally {
            if (rLock.isHeldByCurrentThread) {
                rLock.unlock()
                log.debug("Redis 락 해제: $lockName")
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> runInTransaction(function: () -> T): T = function()
}
