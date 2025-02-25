/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.kafka.spring.event.MessageReadEvent;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class KafkaDltProducerConfiguration {

    KafkaProperties kafkaProperties;

    @Bean
    public RetryTopicConfiguration retryTopic(KafkaTemplate<String, Object> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .autoCreateTopicsWith(2, (short) 3)
                //.doNotAutoCreateRetryTopics()
                .maxAttempts(1)
                .exponentialBackoff(10, 2, 150)
                .retryTopicSuffix("-retry")
                .dltSuffix("-dlt")
                .doNotRetryOnDltFailure()
                .dltProcessingFailureStrategy(DltStrategy.FAIL_ON_ERROR)
                .dltHandlerMethod("messageReadDltProcessor", "processDltMessage")
                .create(template);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        final var configProps = kafkaProperties.getDltProducerProperties();

        var delegatingByTypeSerializer = new DelegatingByTypeSerializer(
                Map.of(
                        byte[].class, new ByteArraySerializer(),
                        String.class, new StringSerializer(),
                        MessageReadEvent.class, new JsonSerializer<>()
                ));

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), delegatingByTypeSerializer);
    }
}