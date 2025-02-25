/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import static java.util.Objects.requireNonNull;
import java.util.UUID;
import lombok.Value;

@Value
public class MessageReadEvent {

    @JsonCreator
    public MessageReadEvent(
            @JsonProperty("eventId") UUID eventId,
            @JsonProperty("userId") UUID userId,
            @JsonProperty("device") String device,
            @JsonProperty("interactionSource") String interactionSource,
            @JsonProperty("interactionType") String interactionType) {

        requireNonNull(eventId);
        requireNonNull(userId);

        this.eventId = eventId;
        this.userId = userId;
        this.device = device;
        this.interactionSource = interactionSource;
        this.interactionType = interactionType;
    }

    UUID eventId;
    UUID userId;
    String device;
    String interactionSource;
    String interactionType;

    @Override
    public String toString() {
        return "MessageReadEvent{" +
                "eventId=" + eventId +
                ", userId=" + userId +
                ", device='" + device + '\'' +
                ", interactionSource='" + interactionSource + '\'' +
                ", interactionType='" + interactionType + '\'' +
                '}';
    }
}
