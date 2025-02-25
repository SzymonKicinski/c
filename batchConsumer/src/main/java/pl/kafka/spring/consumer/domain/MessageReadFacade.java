/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.domain;

import javax.transaction.Transactional;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.kafka.spring.event.MessageReadEvent;
import pl.kafka.spring.consumer.infra.StoreOffsetRegistry;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MessageReadFacade implements MessageReadConsumer {

    MessageReadRepository messageReadRepository;
    StoreOffsetRegistry storeOffsetRegistry;

    @Transactional
    @Override
    public void consume(MessageReadEvent event, String topic, Integer partition, Long offset) {
        final var messageRead = MessageRead.fromMessageReadEvent(event);

        if (hasEventBeenAlreadyProcessed(messageRead)) {
            log.info("Message has been already processed: {}", event.getEventId());
            return;
        }

        processMessage(messageRead);
        messageReadRepository.save(messageRead);
        storeOffsetRegistry.storeConsumedMessageOffset(topic, partition, offset);
    }

    private void processMessage(final MessageRead messageRead) {
        System.out.println("Do Something Useful");
        //processing message

    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean hasEventBeenAlreadyProcessed(final MessageRead messageRead) {
        return messageReadRepository.exists(messageRead.getEventId());
    }
}
