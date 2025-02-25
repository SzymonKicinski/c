/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer.infra;

import java.util.HashMap;
import java.util.Map;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageReadEvent> consumerFactory() {
        final var consumerConfig = getConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

    private Map<String, Object> getConsumerConfig() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000");
        properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "5000");

//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MessageReadEvent.class);
        properties.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return properties;
    }
}