/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Optional;

interface OffsetRepository {

    Optional<StoredOffset> find(final String topic, final Integer partition);

    void save(final StoredOffset storedOffset);
}