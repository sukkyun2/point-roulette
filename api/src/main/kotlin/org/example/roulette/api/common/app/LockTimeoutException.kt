package org.example.roulette.api.common.app

class LockTimeoutException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {
    constructor(lockKey: String, timeoutMs: Long) : this(
        "락 획득에 실패했습니다. 키: $lockKey, 타임아웃: ${timeoutMs}ms",
    )
}
