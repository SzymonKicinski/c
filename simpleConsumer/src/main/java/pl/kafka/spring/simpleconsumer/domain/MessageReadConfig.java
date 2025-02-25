/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MessageReadConfig {

    @Bean
    public MessageReadConsumer messageReadFacade() {
        return new MessageReadFacade();
    }
}