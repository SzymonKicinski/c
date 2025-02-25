/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.HashMap;
import java.util.Map;
import static lombok.AccessLevel.PRIVATE;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.kafka.spring.event.MessageReadEvent;

@FieldDefaults(level = PRIVATE)
@ConfigurationProperties(prefix = "app.kafka")
@Configuration
@Getter
@Setter
class KafkaProperties {

    String groupId;
    String bootstrapServers;

    Map<String, Object> getConsumerConfig() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000");
        properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, "5000");

        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class); // DLT

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        properties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MessageReadEvent.class);
        properties.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        properties.putAll(getSafeProperties());

        return properties;
    }

    Map<String, Object> getSafeProperties() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        return properties;
    }

    Map<String, Object> getFastProperties() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); //default
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000); //default
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 2048);
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);

        return properties;
    }

    Map<String, Object> getDltProducerProperties() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return properties;
    }
}
