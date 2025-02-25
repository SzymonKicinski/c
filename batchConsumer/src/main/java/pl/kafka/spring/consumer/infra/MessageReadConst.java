/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

public interface MessageReadConst {

    interface Listeners {
        String MESSAGE_READ_LISTENER_CONTAINER_FACTORY = "concurrentKafkaListenerContainerFactory";
    }

    interface Groups {
        String MESSAGE_READ_GROUP = "${app.kafka.group-id}";
    }

    // Kafka Topic Name
    interface Topics {
        String MESSAGE_READ_EVENTS = "test-topic-101";
    }
}
