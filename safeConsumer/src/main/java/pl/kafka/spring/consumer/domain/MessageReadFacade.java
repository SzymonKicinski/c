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

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MessageReadFacade implements MessageReadConsumer {

    MessageReadRepository messageReadRepository;

    @Transactional
    @Override
    public void consume(MessageReadEvent event) {
        final var messageRead = MessageRead.fromMessageReadEvent(event);

        if (hasEventBeenAlreadyProcessed(messageRead)) {
            log.info("Message has been already processed: {}", event.getEventId());
            return;
        }

        processMessage(messageRead);
        messageReadRepository.save(messageRead);
    }

    private void processMessage(final MessageRead messageRead) {
        if (messageRead.getInteractionType().equals("error")) {
            throw new RuntimeException("Simulate error");
        }
        System.out.println("Do something useful");
        //processing message
    }

    private boolean hasEventBeenAlreadyProcessed(final MessageRead messageRead) {
        return messageReadRepository.exists(messageRead.getEventId());
    }
}
