/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StoreOffsetConfig {

    @Bean
    public StoreOffsetRegistry storeOffsetRegistry(final OffsetRepository offsetRepository) {
        return new StoreOffsetRegistry(offsetRepository);
    }

    @Bean
    public OffsetRepository offsetRepository(Map<String, StoredOffset> storedOffsets) {
        return new StoreOffsetInMemoryRepository(storedOffsets);
    }

//    @Bean
//    public OffsetRepository offsetRepository() {
//        return new InMemoryOffsetRepository(new ConcurrentHashMap<>());
//    }
}
