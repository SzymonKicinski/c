/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.Collection;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

@Slf4j
@RequiredArgsConstructor
class RebalanceListener implements ConsumerAwareRebalanceListener {

    private final OffsetRepository offsetRepository;
    private final StoreOffsetRegistry storeOffsetRegistry;

    @Override
    public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {

        var offsets = new HashMap<TopicPartition, OffsetAndMetadata>(partitions.size());

        partitions.forEach(partition -> offsetRepository.find(partition.topic(), partition.partition())
                .map(this::toOffsetInfo)
                .ifPresent(offset -> offsets.put(partition, offset)));

        log.info("[REBALANCING_SYNC_COMMIT], offsets {}", offsets.toString());

        consumer.commitSync(offsets);
        printRevokedPartitions(partitions);
    }

    @Override
    public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        printAssignedPartitions(partitions);

        partitions.forEach(
                topicPartition -> onSinglePartitionAssigned(consumer, topicPartition)
        );
    }

    private void onSinglePartitionAssigned(Consumer<?, ?> consumer, TopicPartition topicPartition) {
        storeOffsetRegistry.getLastConsumedMessageOffset(topicPartition)
                .ifPresent(offset -> {
                    log.info("[LAST_COMMITED_OFFSET] offset: {} for topic: {} and partition: {}", offset, topicPartition.topic(), topicPartition.partition());
                    setTopicPartitionOffset(topicPartition, consumer, ++offset);
                });
    }

    private void printAssignedPartitions(Collection<TopicPartition> partitions) {
        partitions.forEach(topicPartition -> log.info("[ON_PARTITION_ASSIGNED] assigned topic: {}, and partition: {}", topicPartition.topic(), topicPartition.partition()));
    }

    private void printRevokedPartitions(Collection<TopicPartition> partitions) {
        partitions.forEach(topicPartition -> log.info("[ON_PARTITION_REVOKED] revoked topic: {}, and partition: {}", topicPartition.topic(), topicPartition.partition()));
    }

    private void setTopicPartitionOffset(final TopicPartition topicPartition, Consumer<?, ?> consumer, Long offset) {
        log.info("[SEEK_TO_SPECIFIC_OFFSET] seek to offset: {} for topic: {} and partition: {}", offset, topicPartition.topic(), topicPartition.partition());
        consumer.seek(topicPartition, offset);
    }

    private OffsetAndMetadata toOffsetInfo(StoredOffset offset) {
        return new OffsetAndMetadata(offset.getReadOffset());
    }
}
