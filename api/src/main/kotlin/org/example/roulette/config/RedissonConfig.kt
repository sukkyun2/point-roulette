package org.example.roulette.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
data class RedissonConfig(
    var host: String = "localhost",
    var port: Int = 6379,
    var database: Int = 0,
) {
    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config
            .useSingleServer()
            .setAddress("redis://$host:$port")
            .setDatabase(database)

        return Redisson.create(config)
    }
}
