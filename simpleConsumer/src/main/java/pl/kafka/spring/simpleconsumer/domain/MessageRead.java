/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.simpleconsumer.domain;

import java.io.Serializable;
import static java.util.Objects.requireNonNull;
import java.util.UUID;
import lombok.Value;
import pl.kafka.spring.event.MessageReadEvent;

@Value
public class MessageRead implements Serializable {

    UUID eventId;
    UUID userId;
    String device;
    String interactionSource;
    String interactionType;

    public static MessageRead fromMessageReadEvent(final MessageReadEvent event) {
        requireNonNull(event.getEventId());
        requireNonNull(event.getUserId());

        return new MessageRead(
                event.getEventId(),
                event.getUserId(),
                event.getDevice(),
                event.getInteractionSource(),
                event.getInteractionType());
    }
}