/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer;

import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.kafka.spring.event.MessageReadEvent;
import pl.kafka.spring.simpleconsumer.domain.MessageReadConsumer;
import pl.kafka.spring.simpleconsumer.infra.MessageReadConst;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Component
class MessageListener {

    MessageReadConsumer messageReadConsumer;

    @KafkaListener(topics = MessageReadConst.Topics.MESSAGE_READ_EVENTS, groupId = MessageReadConst.Groups.MESSAGE_READ_GROUP, containerFactory = MessageReadConst.Listeners.MESSAGE_READ_LISTENER_CONTAINER_FACTORY)
    public void handleMessage(MessageReadEvent event) {

        log.info("[READ MESSAGE] message {}", event.toString());
        messageReadConsumer.consume(event);
    }
}