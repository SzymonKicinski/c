/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer;

import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import pl.kafka.spring.consumer.domain.MessageReadConsumer;
import pl.kafka.spring.consumer.infra.MessageReadConst;
import pl.kafka.spring.event.MessageReadEvent;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class MessageListener {

    MessageReadConsumer messageReadConsumer;

    @KafkaListener(topics = MessageReadConst.Topics.MESSAGE_READ_EVENTS,
            groupId = MessageReadConst.Groups.MESSAGE_READ_GROUP,
            containerFactory = MessageReadConst.Listeners.MESSAGE_READ_LISTENER_CONTAINER_FACTORY)
    public void handleMessage(MessageReadEvent event,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                              @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition,
                              @Header(KafkaHeaders.OFFSET) Long offset,
                              Acknowledgment ack) {

        log.info("[READ MESSAGE] topic: {}, partition: {}, offset: {}, message: {}", topic, partition, offset, event.toString());
        messageReadConsumer.consume(event);
        ack.acknowledge();
    }
}