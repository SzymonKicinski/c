/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Optional;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.TopicPartition;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StoreOffsetRegistry {

    OffsetRepository offsetRepository;

    public Optional<Long> getLastConsumedMessageOffset(final TopicPartition topicPartition) {
        return offsetRepository.find(topicPartition.topic(), topicPartition.partition())
                .map(StoredOffset::getReadOffset);
    }

    public void storeConsumedMessageOffset(final String topic, final Integer partition, final Long offset) {
        final var storedOffset = StoredOffset.builder()
                .topicNamePartition(createTopicNamePartition(topic, partition))
                .readOffset(offset)
                .build();
        offsetRepository.save(storedOffset);
    }

    private String createTopicNamePartition(final String topicName, final Integer partition) {
        return topicName + "-" + partition.toString();
    }
}
