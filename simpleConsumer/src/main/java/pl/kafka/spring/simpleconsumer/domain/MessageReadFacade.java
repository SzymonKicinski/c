/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer.domain;

import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.kafka.spring.event.MessageReadEvent;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MessageReadFacade implements MessageReadConsumer {

    @Override
    public void consume(MessageReadEvent event) {
        final var messageRead = MessageRead.fromMessageReadEvent(event);
        processMessage(messageRead);
    }

    private void processMessage(final MessageRead messageRead) {
        System.out.println("Do something useful");
        //processing message
    }
}