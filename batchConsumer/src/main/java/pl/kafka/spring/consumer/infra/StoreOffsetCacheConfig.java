/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StoreOffsetCacheConfig {

    private final String STORED_OFFSETS_CACHE_NAME = "replicatedStoredOffsetsCache";

    @Bean
    Map<String, StoredOffset> storedOffsetsCache(EmbeddedCacheManager manager, ConfigurationBuilder cb) {
        org.infinispan.configuration.cache.Configuration c = cb.build();
        manager.defineConfiguration(STORED_OFFSETS_CACHE_NAME, c);
        return manager.getCache(STORED_OFFSETS_CACHE_NAME);
    }
}