package com.technicaltest.payment.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class PurchaseOrderEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    Consumer<String, String> currentConsumer;

    public PurchaseOrderEventConsumer(Consumer<String, String> consumer) {
        this.currentConsumer = consumer;
    }

    public void startConsuming() {
        Duration duration = Duration.ofSeconds(1);
        currentConsumer.subscribe(Collections.singleton("offline"));
        while (true) {
            currentConsumer.poll(duration).forEach(message -> {
                        logger.info("Message: " + message.toString());
                    }
            );
            currentConsumer.commitAsync();
        }
    }
}
