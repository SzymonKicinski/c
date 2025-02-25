/*
 * Copyright (c)
 * Author: Szymon Kiciński
 */

package pl.kafka.spring.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafeConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeConsumerApplication.class, args);
    }
}
