/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.LoggingErrorHandler;
import pl.kafka.spring.event.MessageReadEvent;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class KafkaConsumerConfiguration {

    KafkaProperties kafkaProperties;

    @Bean(MessageReadConst.Listeners.MESSAGE_READ_LISTENER_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, MessageReadEvent> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, MessageReadEvent>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setSyncCommits(true);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setErrorHandler(new LoggingErrorHandler());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageReadEvent> consumerFactory() {
        final var consumerConfig = kafkaProperties.getConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }
}