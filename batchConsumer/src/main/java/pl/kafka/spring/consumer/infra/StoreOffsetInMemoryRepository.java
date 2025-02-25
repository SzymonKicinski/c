/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Map;
import java.util.Optional;
import static lombok.AccessLevel.PRIVATE;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class StoreOffsetInMemoryRepository implements OffsetRepository {

    //topicName-partition as key
    Map<String, StoredOffset> storedOffsets;

    @Override
    public Optional<StoredOffset> find(final String topic, final Integer partition) {
        final var key = createKey(topic, partition);
        return storedOffsets.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(key))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public void save(final StoredOffset storedOffset) {
        storedOffsets.put(storedOffset.getKey(), storedOffset);
    }

    private String createKey(String topic, Integer partition) {
        return topic + "-" + partition;
    }
}