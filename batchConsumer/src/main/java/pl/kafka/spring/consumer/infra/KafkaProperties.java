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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = PRIVATE)
@ConfigurationProperties(prefix = "app.kafka")
@Configuration
@Getter
@Setter
class KafkaProperties {

    String groupId;
    String bootstrapServers;

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
}
