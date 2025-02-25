/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kafka.spring.consumer.domain.MessageRead;
import pl.kafka.spring.consumer.domain.MessageReadRepository;

@Configuration
class MessageReadRepositoryConfig {

    @Bean
    public MessageReadRepository messageReadRepository(Map<UUID, MessageRead> messagesRead) {
        return new MessageReadInMemoryRepository(messagesRead);
    }

// For test InMemory Data Base
//    @Bean
//    public MessageReadRepository messageReadRepository() {
//        return new InMemoryMessageReadRepository(new ConcurrentHashMap<>());
//    }
}
