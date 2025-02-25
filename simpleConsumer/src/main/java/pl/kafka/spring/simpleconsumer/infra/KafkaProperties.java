/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer.infra;

import static lombok.AccessLevel.PRIVATE;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
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
}