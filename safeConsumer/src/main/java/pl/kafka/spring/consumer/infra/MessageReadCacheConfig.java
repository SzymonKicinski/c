/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kafka.spring.consumer.domain.MessageRead;

@Configuration
class MessageReadCacheConfig {

    private final String MESSAGE_READ_CACHE_NAME = "replicatedMessageReadCache";
    private final long CACHE_EXPIRATION_TIME_IN_DAYS = 14;

    @Bean
    Map<UUID, MessageRead> messageReadCache(EmbeddedCacheManager manager, ConfigurationBuilder cb) {
        cb.expiration().maxIdle(CACHE_EXPIRATION_TIME_IN_DAYS, TimeUnit.DAYS);
        org.infinispan.configuration.cache.Configuration c = cb.build();
        manager.defineConfiguration(MESSAGE_READ_CACHE_NAME, c);
        return manager.getCache(MESSAGE_READ_CACHE_NAME);
    }
}