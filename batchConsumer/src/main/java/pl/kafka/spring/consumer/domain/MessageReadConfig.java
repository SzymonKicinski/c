/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kafka.spring.consumer.infra.StoreOffsetRegistry;

@Configuration
class MessageReadConfig {

    @Bean
    public MessageReadConsumer messageReadFacade(final MessageReadRepository messageReadRepository, final StoreOffsetRegistry storeOffsetRegistry) {
        return new MessageReadFacade(messageReadRepository, storeOffsetRegistry);
    }
}
