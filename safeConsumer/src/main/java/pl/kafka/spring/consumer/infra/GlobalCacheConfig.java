/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer.infra;

import java.util.UUID;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kafka.spring.consumer.domain.MessageRead;

@Configuration
class GlobalCacheConfig {

    @Bean
    EmbeddedCacheManager embeddedCacheManager() {
        return new DefaultCacheManager(getConfig());
    }

    @Bean
    ConfigurationBuilder configurationBuilder() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.clustering().cacheMode(CacheMode.REPL_SYNC);
        addPersistence(cb);
        return cb;
    }

    private GlobalConfiguration getConfig() {
        GlobalConfigurationBuilder globalConfig = new GlobalConfigurationBuilder();

        globalConfig
                .transport().transport()
                .defaultTransport()
                .clusterName("qa-cluster")
                .addProperty("configurationFile", "default-configs/default-jgroups-udp.xml");

        globalConfig
                .serialization()
                .marshaller(new JavaSerializationMarshaller())
                .allowList()
                .addClasses(UUID.class, MessageRead.class); // klasy ktore beda serializowane

        return globalConfig.build();
    }

    public static void addPersistence(ConfigurationBuilder cb) {
        cb.persistence()
                .addStore(JdbcStringBasedStoreConfigurationBuilder.class)
                .shared(true) // same data store for all nodes
                //.async() // write-behind (write through if not mentioned)
                .preload(true) // load data on startup
                .fetchPersistentState(false) // false, data will be transferred in shared cache over network
                .ignoreModifications(false)
                .purgeOnStartup(false)
                .table()
                .dropOnExit(false)
                .createOnStart(true)
                .tableNamePrefix("ISPN_STRING_TABLE")
                .idColumnName("ID_COLUMN").idColumnType("VARCHAR(255)")
                .dataColumnName("DATA_COLUMN").dataColumnType("BYTEA")
                .timestampColumnName("TIMESTAMP_COLUMN").timestampColumnType("BIGINT")
                .segmentColumnName("SEGMENT_COLUMN").segmentColumnType("INT")
                .connectionPool()
                .connectionUrl("jdbc:postgresql://localhost:5432/dbcache")
                .username("postgres")
                .password("postgres")
                .driverClass("org.postgresql.Driver");
    }
}