/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer;

import java.util.concurrent.atomic.AtomicInteger;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    static Integer COMMIT_INTERVAL = 100;

    @KafkaListener(topics = MessageReadConst.Topics.MESSAGE_READ_EVENTS, groupId = MessageReadConst.Groups.MESSAGE_READ_GROUP, containerFactory = MessageReadConst.Listeners.MESSAGE_READ_LISTENER_CONTAINER_FACTORY)
    public void handleMessage(ConsumerRecords<String, MessageReadEvent> events, Acknowledgment ack) {
        AtomicInteger i = new AtomicInteger(0);
        events.iterator().forEachRemaining(event -> processSingleMessage(ack, i, event));
        commitOffset(ack);
    }

    private void processSingleMessage(Acknowledgment ack, AtomicInteger i, org.apache.kafka.clients.consumer.ConsumerRecord<String, MessageReadEvent> event) {
        final var offset = event.offset();
        final var partition = event.partition();
        final var topic = event.topic();
        final var eventValue = event.value();

        log.info("[START CONSUMING MESSAGE] topic: {}, partition: {}, offset: {}, messag: {}", topic, partition, offset, event.toString());

        messageReadConsumer.consume(eventValue, topic, partition, offset);

        if (isCommitMoment(i)) {
            commitOffset(ack);
        }
    }

    private boolean isCommitMoment(AtomicInteger i) {
        return i.incrementAndGet() % COMMIT_INTERVAL == 0;
    }

    private void commitOffset(Acknowledgment ack) {
        ack.acknowledge();
    }
}